package com.luiza.ex4_lancheriaddd_v1.Aplicacao.Responses;

import java.time.LocalDateTime;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

public record StatusPedidoResponse(
    long id,
    Pedido.Status status,
    double valorTotal,
    double desconto,
    LocalDateTime dataHoraPagamento
) {}
