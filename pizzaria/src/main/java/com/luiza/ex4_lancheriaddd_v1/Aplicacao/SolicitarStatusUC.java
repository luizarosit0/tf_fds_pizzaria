package com.luiza.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.beans.factory.annotation.Autowired;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.luiza.ex4_lancheriaddd_v1.Aplicacao.Responses.StatusPedidoResponse;

public class SolicitarStatusUC {
    private  PedidoRepository pedidoRepository;

    @Autowired
    public SolicitarStatusUC(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public StatusPedidoResponse run(long idPedido) {
        Pedido pedido = pedidoRepository.buscarPorId(idPedido);
        if (pedido == null) return null;
        return new StatusPedidoResponse(
            pedido.getId(),
            pedido.getStatus(),
            pedido.getValorCobrado(),
            pedido.getDesconto(),
            pedido.getDataHoraPagamento()
        );
    }
}
