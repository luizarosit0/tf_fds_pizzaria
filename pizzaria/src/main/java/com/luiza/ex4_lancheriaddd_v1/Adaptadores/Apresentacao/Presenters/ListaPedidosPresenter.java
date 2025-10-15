package com.luiza.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.Presenters;

import java.util.List;
import java.util.stream.Collectors;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

public record ListaPedidosPresenter(List<PedidoPresenter> pedidos) {

    public static ListaPedidosPresenter fromEntityList(List<Pedido> pedidos) {
        List<PedidoPresenter> presenters = pedidos.stream()
            .map(PedidoPresenter::new)
            .collect(Collectors.toList());
        return new ListaPedidosPresenter(presenters);
    }
}