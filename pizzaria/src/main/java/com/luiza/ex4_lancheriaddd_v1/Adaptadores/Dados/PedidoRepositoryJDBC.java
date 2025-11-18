package com.luiza.ex4_lancheriaddd_v1.Adaptadores.Dados;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

        String sqlPedido = "INSERT INTO pedidos (cliente_cpf, status, data_hora_pagamento, valor, impostos, desconto, valor_cobrado) " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?)";

        // objeto do Spring que armazena as chaves geradas pelo banco de dados depois de um INSERT.
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, pedido.getCliente().getCpf());
            ps.setString(2, pedido.getStatus().name());
            //ps.setObject(3, pedido.getDataHoraPagamento()); 

            if (pedido.getDataHoraPagamento() != null) {
                ps.setObject(3, pedido.getDataHoraPagamento());
            } else {
                ps.setNull(3, Types.TIMESTAMP); // Define um NULL do tipo TIMESTAMP
            }

            ps.setBigDecimal(4, BigDecimal.valueOf(pedido.getValor()));
            ps.setBigDecimal(5, BigDecimal.valueOf(pedido.getImpostos()));
            ps.setBigDecimal(6, BigDecimal.valueOf(pedido.getDesconto()));
            ps.setBigDecimal(7, BigDecimal.valueOf(pedido.getValorCobrado()));
            return ps;
        }, keyHolder);

        // pegamar o ID gerado do KeyHolder
        Number generatedId = keyHolder.getKey();
        if (generatedId == null) {
            // Se o ID for nulo, lançamos um erro claro em vez de uma NullPointerException
            throw new IllegalStateException("Falha ao salvar o pedido: O banco de dados não retornou o ID gerado.");
        }
        long pedidoId = generatedId.longValue();

        pedido.setId(pedidoId);

        String sqlItemPedido = "INSERT INTO itens_pedido (pedido_id, produto_id, quantidade) VALUES (?, ?, ?)";
        
        for (ItemPedido item : pedido.getItens()) {
            jdbcTemplate.update(sqlItemPedido, pedidoId, item.getItem().getId(), item.getQuantidade());
        }

        return pedido;
    }

    @Override
    public Pedido buscarPorId(long id) {
        try {
            //buscar o pedido e os dados do cliente
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
                    null,
                    Pedido.Status.valueOf(rs.getString("status")),
                    rs.getDouble("valor"),
                    rs.getDouble("impostos"),
                    rs.getDouble("desconto"),
                    rs.getDouble("valor_cobrado")
                );
            }, id);

            // buscar os itens
            String sqlItens = "SELECT ip.quantidade, prod.id as prod_id, prod.descricao, prod.preco " +
                              "FROM itens_pedido ip JOIN produtos prod ON ip.produto_id = prod.id " +
                              "WHERE ip.pedido_id = ?";
                              
            List<ItemPedido> itens = jdbcTemplate.query(sqlItens, (rs, rowNum) -> {
                // produto simplificado
                Produto produto = new Produto(
                    rs.getLong("prod_id"),
                    rs.getString("descricao"),
                    new Receita(0, "", null), // uma receita "fake" para satisfazer o construtor
                    rs.getInt("preco")
                );
                return new ItemPedido(produto, rs.getInt("quantidade"));
            }, id);

            pedido.setItens(itens);
            return pedido;

        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void atualizar(Pedido pedido) {
        String sql = "UPDATE pedidos SET status = ?, data_hora_pagamento = ?, " +
                    "valor = ?, impostos = ?, desconto = ?, valor_cobrado = ? " +
                    "WHERE id = ?";

        jdbcTemplate.update(sql,
            pedido.getStatus().name(),
            pedido.getDataHoraPagamento(),
            BigDecimal.valueOf(pedido.getValor()),
            BigDecimal.valueOf(pedido.getImpostos()), 
            BigDecimal.valueOf(pedido.getDesconto()), 
            BigDecimal.valueOf(pedido.getValorCobrado()),
            pedido.getId()
        );
    }

    @Override
    public List<Pedido> buscarPorStatusEPeriodo(Pedido.Status status, LocalDateTime dataInicial, LocalDateTime dataFinal) {
        
        String sql = "SELECT p.id FROM pedidos p WHERE p.status = ? AND p.data_hora_pagamento BETWEEN ? AND ?";

        // pega os IDs que estão entre as duas datas
        List<Long> ids = jdbcTemplate.queryForList(sql, Long.class, 
                                                   status.name(), 
                                                   dataInicial, 
                                                   dataFinal);

        //pra cada um, chamar buscarPorId() 
        return ids.stream()
                  .map(this::buscarPorId)
                  .toList();
    }

    @Override
    public int quantPedidos(String CPF, int dias) {
        String sql = "SELECT COUNT(*) FROM pedidos WHERE cliente_cpf = ? AND data_hora_pagamento >= ?";
        LocalDate dataLimite = LocalDate.now().minusDays(dias);
        
        return jdbcTemplate.queryForObject(sql, Integer.class, CPF, dataLimite);
    }

    @Override
    public double totalGastoUltimosDias(String CPF, int dias) {
        String sql = "SELECT COALESCE(SUM(valor), 0) FROM pedidos WHERE cliente_cpf = ? AND data_hora_pagamento >= ?";
        LocalDate dataLimite = LocalDate.now().minusDays(dias);

        // retorna o total somado ou 0
        Double total = jdbcTemplate.queryForObject(sql, Double.class, CPF, dataLimite);

        return total != null ? total : 0.0;
    }
}
