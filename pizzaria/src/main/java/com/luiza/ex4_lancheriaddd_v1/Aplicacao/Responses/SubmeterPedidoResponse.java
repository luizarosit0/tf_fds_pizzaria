package com.luiza.ex4_lancheriaddd_v1.Aplicacao.Responses;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

public class SubmeterPedidoResponse {
    private Pedido pedido;

    public SubmeterPedidoResponse(Pedido pedido) {
        this.pedido = pedido;
    }

    public Pedido getPedido() {
        return pedido;
    }
}
