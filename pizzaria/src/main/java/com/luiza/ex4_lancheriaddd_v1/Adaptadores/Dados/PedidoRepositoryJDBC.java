package com.luiza.ex4_lancheriaddd_v1.Adaptadores.Dados;

import java.util.HashMap;
import java.util.Map;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

public class PedidoRepositoryJDBC implements PedidoRepository { // arrumar e criar com o template do JDBC

    private static final Map<Long, Pedido> bancoFake = new HashMap<>();

    @Override
    public void salvar(Pedido pedido) {
        bancoFake.put(pedido.getId(), pedido);
    }

    @Override
    public Pedido buscarPorId(long id) {
        return bancoFake.get(id);
    }
}
