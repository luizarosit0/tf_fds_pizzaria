package com.luiza.ex4_lancheriaddd_v1;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

import com.luiza.ex4_lancheriaddd_v1.Adaptadores.Dados.PedidoRepositoryJDBC;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Receita;

import jakarta.transaction.Transactional;

// Testar a integração entre o repositorio e o banco H2 
@SpringBootTest
@Transactional 
public class PedidoRepositoryIntegrationTests {

    private JdbcTemplate jdbcTemplate;
    private PedidoRepositoryJDBC pedidoRepository;

    private final String CPF_TESTE = "12345678900";

    @Autowired
    public PedidoRepositoryIntegrationTests(JdbcTemplate jdbcTemplate, PedidoRepositoryJDBC pedidoRepository){
        this.jdbcTemplate = jdbcTemplate;
        this.pedidoRepository = pedidoRepository;
    }

    // Como JbdcTest apaga tudo, antes de cada um tem que ter o Cliente e o Produto
    // SQL direto -> para isolar e não depender de outras 
    @BeforeEach 
    void setupBanco() {
        // 1. Limpeza rigorosa na ordem inversa das chaves estrangeiras
    
    // NÍVEL 3 (Netos) - Tabelas de relacionamento
    jdbcTemplate.update("DELETE FROM itens_pedido");
    jdbcTemplate.update("DELETE FROM cardapio_produto");
    jdbcTemplate.update("DELETE FROM produto_receita");
    jdbcTemplate.update("DELETE FROM receita_ingrediente");
    
    // NÍVEL 2 (Filhos) - Tabelas que dependem de Pais (INGREDIENTES)
    // ESTA LINHA PRECISA VIR ANTES DE 'DELETE FROM ingredientes'
    jdbcTemplate.update("DELETE FROM itensestoque"); // <-- A CORREÇÃO FINAL
    
    // NÍVEL 2 (Filhos) - Tabela de Pedidos (limpar e resetar o contador)
    jdbcTemplate.update("DELETE FROM pedidos"); 
    jdbcTemplate.update("ALTER TABLE pedidos ALTER COLUMN id RESTART WITH 1"); // Garante ID=1 para o teste
    
    // NÍVEL 1 (Pais) - Tabelas Pais
    jdbcTemplate.update("DELETE FROM produtos");
    jdbcTemplate.update("DELETE FROM clientes");
    jdbcTemplate.update("DELETE FROM receitas");
    jdbcTemplate.update("DELETE FROM ingredientes"); // Agora deve funcionar
    jdbcTemplate.update("DELETE FROM cardapios"); 
    jdbcTemplate.update("DELETE FROM usuarios");
    
    // 2. Inserir os dados fixos necessários para este teste
    String CPF_TESTE = "12345678900"; 
    jdbcTemplate.update("INSERT INTO clientes (cpf, nome, celular, endereco, email) VALUES (?, ?, ?, ?, ?)",
        CPF_TESTE, "Cliente Teste", "99999999", "Rua Teste", "teste@email.com");
    
    // Inserir um produto para o teste (ID 99)
    jdbcTemplate.update("INSERT INTO produtos (id, descricao, preco) VALUES (?, ?, ?)",
        99L, "Pizza Teste", 5000); 
    }

    /*
     * Caso de Teste 1: salvar pedido e recuperar ID
     * Entradas: objeto Pedido em memória com valores fixos
     * Estado inicial do banco: Cliente ('12345678900') e Produto (ID 1) inseridos no H2
     * Resultados esperados: O objeto Pedido retornado deve ter um ID gerado (> 0 e not null) 
     *                       Deve ter exatamente 1 registro na tabela pedidos com esse ID
     */
    @Test
    @DisplayName("Deve salvar um pedido e gerar um ID")
    void salvaPedidoGerarId() {
        // cria objeto Pedido em memória
        Cliente cliente = new Cliente(CPF_TESTE, "Cliente Teste", "99", "Rua", "email");
        Produto produto = new Produto(99L, "Pizza Teste", new Receita(0, "", null), 5000);
        ItemPedido item = new ItemPedido(produto, 2); // 2 pizzas = R$ 100,00
        
        Pedido novoPedido = new Pedido(cliente, List.of(item));
        novoPedido.setValor(100.00);
        novoPedido.setImpostos(10.00);
        novoPedido.setValorCobrado(110.00);

        Pedido pedidoSalvo = pedidoRepository.salvar(novoPedido);

        // verifica se salvou
        assertNotNull(pedidoSalvo.getId(), "O ID não deveria ser nulo");
        assertTrue(pedidoSalvo.getId() > 0, "O ID deve ser maior que 0");
        
        // consulta o banco para ver se gravou mesmo
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM pedidos WHERE id = ?", Integer.class, pedidoSalvo.getId());
        assertEquals(1, count, "O pedido deve existir no banco de dados");
    }

    /*
     * Caso de Teste 2: Contagem de pedidos recentes
     * Entradas: CPF de teste e período de 20 dias.
     * Estado inicial do banco: 4 pedidos inseridos, sendo 3 nos últimos 10 dias (dentro do prazo de 20) e 1 há 30 dias (fora do prazo).
     * Resultados esperados: Retorno da contagem deve ser 3.
     */
    @Test
    @DisplayName("Deve contar pedidos recentes corretamente")
    void contaPedidosRecentes() {
        // insere 4 pedidos manualmente no banco, com datas especificas
        String sqlInsert = "INSERT INTO pedidos (cliente_cpf, status, data_hora_pagamento, valor, impostos, desconto, valor_cobrado) VALUES (?, 'PAGO', ?, 100, 10, 0, 110)";
        
        jdbcTemplate.update(sqlInsert, CPF_TESTE, LocalDateTime.now().minusDays(1)); // Ontem
        jdbcTemplate.update(sqlInsert, CPF_TESTE, LocalDateTime.now().minusDays(5)); // 5 dias atras
        jdbcTemplate.update(sqlInsert, CPF_TESTE, LocalDateTime.now().minusDays(10)); // 10 dias atras
        // inserir um pedido de mais de 20 dias, para garantir que não conta
        jdbcTemplate.update(sqlInsert, CPF_TESTE, LocalDateTime.now().minusDays(30)); // 30 dias atras

        int quantidade = pedidoRepository.quantPedidos(CPF_TESTE, 20);

        assertEquals(3, quantidade, "Deve encontrar exatamente 3 pedidos nos últimos 20 dias");
    }

    /*
     * Caso de Teste 3: Somatória de gasto total
     * Objetivo: Validar se a função de agregação SQL (SUM) retorna o valor total dos pedidos 
*                pagos dentro dos últimos N dias para um dado CPF.
     * Entradas: CPF de teste e período de 30 dias.
     * Estado inicial do banco: 2 pedidos inseridos com valores 200.00 e 350.50.
     * Resultados esperados: Retorno do total gasto deve ser 550.50 
     */
    @Test
    @DisplayName("Deve somar total gasto corretamente")
    void somaTotalGasto() {
        // Insere pedidos com valores diferentes
        jdbcTemplate.update("INSERT INTO pedidos (cliente_cpf, status, data_hora_pagamento, valor, impostos, desconto, valor_cobrado) VALUES (?, 'PAGO', ?, 200.00, 20, 0, 220)", 
            CPF_TESTE, LocalDateTime.now().minusDays(2)); // R$ 200,00 
        jdbcTemplate.update("INSERT INTO pedidos (cliente_cpf, status, data_hora_pagamento, valor, impostos, desconto, valor_cobrado) VALUES (?, 'PAGO', ?, 350.50, 35, 0, 385)", 
            CPF_TESTE, LocalDateTime.now().minusDays(5)); // R$ 350,50

        double total = pedidoRepository.totalGastoUltimosDias(CPF_TESTE, 30);

        assertEquals(550.50, total, 0.01, "O total deve ser a soma dos valores base (200 + 350.50)");
    }
}
