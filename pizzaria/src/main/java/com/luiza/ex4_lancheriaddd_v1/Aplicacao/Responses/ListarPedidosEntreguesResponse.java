package com.luiza.ex4_lancheriaddd_v1.Aplicacao.Responses;

import java.util.List;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

public record ListarPedidosEntreguesResponse(List<Pedido> pedidos) {}
