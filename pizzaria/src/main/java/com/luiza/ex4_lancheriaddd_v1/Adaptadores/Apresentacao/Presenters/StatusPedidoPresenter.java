package com.luiza.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.Presenters;

import java.time.LocalDateTime;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

public record StatusPedidoPresenter(
    long id,
    Pedido.Status status,
    double valorTotal,
    double desconto,
    LocalDateTime dataHoraPagamento
) {}
