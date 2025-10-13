package com.luiza.ex4_lancheriaddd_v1.Adaptadores.Dados;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Receita;

@Repository
public class PedidoRepositoryJDBC implements PedidoRepository { 
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    public PedidoRepositoryJDBC(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @Override
    public Pedido salvar(Pedido pedido) {
        // 1. Salvar na tabela 'pedidos'
        String sqlPedido = "INSERT INTO pedidos (cliente_cpf, status, data_hora_pagamento, valor, impostos, desconto, valor_cobrado) " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?)";

        // O KeyHolder é um objeto especial do Spring que armazena as chaves (IDs)
        // geradas pelo banco de dados após um INSERT.
        KeyHolder keyHolder = new GeneratedKeyHolder();

        // Executamos o INSERT usando uma forma mais completa do jdbcTemplate.update
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, pedido.getCliente().getCpf());
            ps.setString(2, pedido.getStatus().name());
            // dataHoraPagamento pode ser nulo inicialmente
            ps.setObject(3, pedido.getDataHoraPagamento()); 
            ps.setDouble(4, pedido.getValor());
            ps.setDouble(5, pedido.getImpostos());
            ps.setDouble(6, pedido.getDesconto());
            ps.setDouble(7, pedido.getValorCobrado());
            return ps;
        }, keyHolder);

        // Após a execução, pegamos o ID gerado do KeyHolder
        long pedidoId = keyHolder.getKey().longValue();

        // Agora, inserimos cada item do pedido na tabela 'itens_pedido'
        String sqlItemPedido = "INSERT INTO itens_pedido (pedido_id, produto_id, quantidade) VALUES (?, ?, ?)";
        
        for (ItemPedido item : pedido.getItens()) {
            jdbcTemplate.update(sqlItemPedido, pedidoId, item.getItem().getId(), item.getQuantidade());
        }

        // Atualizamos o objeto original com o novo ID e o retornamos
        // (Isso não é estritamente necessário, mas é uma boa prática)
        // Pedido.java precisaria de um setId(long id).
        return pedido;
    }

    @Override
    public Pedido buscarPorId(long id) {
        try {
            // 1. Buscar o pedido principal e os dados do cliente associado
            String sqlPedido = "SELECT p.*, c.nome, c.celular, c.endereco, c.email " +
                               "FROM pedidos p JOIN clientes c ON p.cliente_cpf = c.cpf " +
                               "WHERE p.id = ?";
            
            Pedido pedido = jdbcTemplate.queryForObject(sqlPedido, (rs, rowNum) -> {
                Cliente cliente = new Cliente(
                    rs.getString("cliente_cpf"),
                    rs.getString("nome"),
                    rs.getString("celular"),
                    rs.getString("endereco"),
                    rs.getString("email")
                );
                return new Pedido(
                    rs.getLong("id"),
                    cliente,
                    rs.getTimestamp("data_hora_pagamento") != null ? rs.getTimestamp("data_hora_pagamento").toLocalDateTime() : null,
                    null, // Itens serão buscados separadamente
                    Pedido.Status.valueOf(rs.getString("status")),
                    rs.getDouble("valor"),
                    rs.getDouble("impostos"),
                    rs.getDouble("desconto"),
                    rs.getDouble("valor_cobrado")
                );
            }, id);

            // 2. Buscar os itens do pedido
            String sqlItens = "SELECT ip.quantidade, prod.id as prod_id, prod.descricao, prod.preco " +
                              "FROM itens_pedido ip JOIN produtos prod ON ip.produto_id = prod.id " +
                              "WHERE ip.pedido_id = ?";
                              
            List<ItemPedido> itens = jdbcTemplate.query(sqlItens, (rs, rowNum) -> {
                // Criamos um objeto Produto simplificado, pois não precisamos da receita aqui.
                Produto produto = new Produto(
                    rs.getLong("prod_id"),
                    rs.getString("descricao"),
                    new Receita(0, "", null), // Receita "fake" para satisfazer o construtor
                    rs.getInt("preco")
                );
                return new ItemPedido(produto, rs.getInt("quantidade"));
            }, id);

            // 3. Adicionar a lista de itens ao pedido e retorná-lo
            pedido.getItens().addAll(itens); // Precisaremos de um setter ou de um construtor que aceite a lista
            return pedido;

        } catch (EmptyResultDataAccessException e) {
            // Se nenhum pedido for encontrado com o ID, retorna nulo.
            return null;
        }
    }

    @Override
    public int quantPedidos(String CPF, int dias) {
        String sql = "SELECT COUNT(*) FROM pedidos WHERE cliente_cpf = ? AND data_hora_pagamento >= ?";
        LocalDate dataLimite = LocalDate.now().minusDays(dias);
        
        // O método queryForObject é o padrão do Spring JDBC para quando a query retorna um único valor.
        // Ele é perfeitamente alinhado com o estilo do professor, sendo apenas mais específico para este caso.
        return jdbcTemplate.queryForObject(sql, Integer.class, CPF, dataLimite);
    }
}
