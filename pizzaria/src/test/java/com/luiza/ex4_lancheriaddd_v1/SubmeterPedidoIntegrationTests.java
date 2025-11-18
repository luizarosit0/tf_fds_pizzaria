package com.luiza.ex4_lancheriaddd_v1;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.luiza.ex4_lancheriaddd_v1.Aplicacao.Responses.SubmeterPedidoResponse;
import com.luiza.ex4_lancheriaddd_v1.Aplicacao.SubmeterPedidoUC;
import com.luiza.ex4_lancheriaddd_v1.Aplicacao.SubmeterPedidoUC.ItemData;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido.Status;

@SpringBootTest // carrega tudo  
@Transactional  // rollback depois de cada teste 
public class SubmeterPedidoIntegrationTests {

    private final String CPF_CLIENTE = "99988877700";
    private final long ID_PRODUTO = 50L;
    private final long ID_RECEITA = 50L;
    private final long ID_INGREDIENTE = 500L;

    private SubmeterPedidoUC submeterPedidoUC; 
    private JdbcTemplate jdbcTemplate;

    @Autowired 
    public SubmeterPedidoIntegrationTests(SubmeterPedidoUC submeterPedidoUC, JdbcTemplate jdbcTemplate){
        this.submeterPedidoUC = submeterPedidoUC;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    void setup() {
        // 1. LIMPEZA TOTAL (Mantenha a limpeza completa para garantir o isolamento)
        jdbcTemplate.update("DELETE FROM itens_pedido");
        jdbcTemplate.update("DELETE FROM cardapio_produto");
        jdbcTemplate.update("DELETE FROM produto_receita");
        jdbcTemplate.update("DELETE FROM receita_ingrediente");
        jdbcTemplate.update("DELETE FROM itensEstoque"); 
        jdbcTemplate.update("DELETE FROM pedidos"); 
        jdbcTemplate.update("DELETE FROM produtos");
        jdbcTemplate.update("DELETE FROM clientes");
        jdbcTemplate.update("DELETE FROM receitas");
        jdbcTemplate.update("DELETE FROM ingredientes");
        jdbcTemplate.update("DELETE FROM cardapios");
        jdbcTemplate.update("DELETE FROM usuarios");

        // criar: Cliente + Ingrediente + Estoque + Receitas + Produto        
        jdbcTemplate.update("INSERT INTO clientes (cpf, nome, celular, endereco, email) VALUES (?, ?, ?, ?, ?)",
                CPF_CLIENTE, "Cliente Integracao", "519999999", "Rua Teste", "integ@email.com");

        jdbcTemplate.update("INSERT INTO ingredientes (id, descricao) VALUES (?, ?)", ID_INGREDIENTE, "Farinha Mágica");

        jdbcTemplate.update("INSERT INTO itensEstoque (id, quantidade, ingrediente_id) VALUES (?, ?, ?)", 
            10L, 10000, ID_INGREDIENTE);

        jdbcTemplate.update("INSERT INTO receitas (id, titulo) VALUES (?, ?)", ID_RECEITA, "Receita Mágica");
        jdbcTemplate.update("INSERT INTO receita_ingrediente (receita_id, ingrediente_id, quantidade_necessaria) VALUES (?, ?, ?)", 
            ID_RECEITA, ID_INGREDIENTE, 1);

        jdbcTemplate.update("INSERT INTO produtos (id, descricao, preco) VALUES (?, ?, ?)", ID_PRODUTO, "Pizza Mágica", 6000);
        jdbcTemplate.update("INSERT INTO produto_receita (produto_id, receita_id) VALUES (?, ?)", ID_PRODUTO, ID_RECEITA);
    }

    /*
     * Caso de Teste 1: Submissão de pedido com sucesso
     * Objetivo: Verificar o fluxo completo do SubmeterPedidoUC, garantindo a integração entre 
*                Services e Repositories/Banco
     * Entradas: Cliente CPF ('99988877700'), Itens: 2x Pizza Mágica (ID 50).
     * Estado inicial do banco: Cliente, Produto, Receita, Ingrediente e Estoque (100 unidades) pré-inseridos no H2.
     * Resultados esperados: Retorno do SubmeterPedidoResponse com status APROVADO e valorCobrado 
     *                       de 132.00 (120.00 + 10% imposto, sem desconto).
     */
    @Test
    @DisplayName("Deve processar pedido com sucesso quando tudo está ok")
    void submetePedidoComSucesso() {
        // simula a entrada do Controller
        List<ItemData> itens = List.of(new ItemData(ID_PRODUTO, 2)); // 2 Pizzas

        SubmeterPedidoResponse response = submeterPedidoUC.run(CPF_CLIENTE, itens);

        assertNotNull(response);
        assertNotNull(response.getPedido());
        assertEquals(Status.APROVADO, response.getPedido().getStatus());
        
        // Verificações:
        // Valor esperado: 2 * 60.00 = 120.00
        // Imposto (10%): 12.00
        // Total com imposto: 132.00 (sem desconto pois é cliente novo)
        assertEquals(132.00, response.getPedido().getValorCobrado(), 0.01);
    }

    /*
     * Caso de Teste 2: Falha na submissão por estoque insuficiente
     * Objetivo: Validar a regra de negócio do EstoqueService/PedidoService, verificando se o 
*                pedido é rejeitado (Status.CANCELADO) quando o estoque é insuficiente.
     * Entradas: Cliente CPF ('99988877700'), Itens: 2x Pizza Mágica (ID 50).
     * Estado inicial do banco: Cliente, Produto, Receita, Ingrediente. 
     *                          Ação no Setup: Quantidade do itensEstoque alterada para 0.
     * Resultados Esperados: O método submeterPedidoUC.run() deve retornar um 
     *                       SubmeterPedidoResponse contendo um Pedido com Status.CANCELADO.
     */
    @Test
    @DisplayName("Deve cancelar o pedido se não houver estoque suficiente")
    void cancelaSeSemEstoque() {
        // Atualiza o registro de estoque, que o setup criou, para 0 intens 
        jdbcTemplate.update("UPDATE itensEstoque SET quantidade = ? WHERE ingrediente_id = ?", 
                            0, ID_INGREDIENTE); 

        List<ItemData> itens = List.of(new ItemData(ID_PRODUTO, 2)); 

        SubmeterPedidoResponse response = submeterPedidoUC.run(CPF_CLIENTE, itens);

        assertNotNull(response);
        assertNotNull(response.getPedido());
        assertEquals(Status.CANCELADO, response.getPedido().getStatus(), 
                     "O status do pedido deveria ser CANCELADO devido à falta de estoque.");
    }
}
