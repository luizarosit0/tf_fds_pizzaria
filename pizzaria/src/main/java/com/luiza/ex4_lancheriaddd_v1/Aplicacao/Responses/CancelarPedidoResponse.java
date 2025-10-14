package com.luiza.ex4_lancheriaddd_v1.Aplicacao.Responses;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

public record CancelarPedidoResponse(
    long id,
    Pedido.Status status,
    String mensagem
) {}
