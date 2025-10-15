package com.luiza.ex4_lancheriaddd_v1.Dominio.Dados;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

public interface PedidoRepository {
    Pedido salvar(Pedido pedido);
    Pedido buscarPorId(long id);
    void atualizar(Pedido pedido);
    // metodo para o DescontoService
    int quantPedidos(String CPF, int dias);
}
