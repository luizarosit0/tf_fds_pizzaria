package com.luiza.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.Presenters;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

public record CancelarPedidoPresenter(
    long id,
    Pedido.Status status,
    String mensagem
) {}
//