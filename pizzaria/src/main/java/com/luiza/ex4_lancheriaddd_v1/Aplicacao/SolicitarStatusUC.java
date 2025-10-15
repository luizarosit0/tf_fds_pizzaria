package com.luiza.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.luiza.ex4_lancheriaddd_v1.Aplicacao.Responses.StatusPedidoResponse;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

@Component
public class SolicitarStatusUC {
    private  PedidoRepository pedidoRepository;

    @Autowired
    public SolicitarStatusUC(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public StatusPedidoResponse run(long idPedido) {
        Pedido pedido = pedidoRepository.buscarPorId(idPedido);
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido com ID " + idPedido + " não encontrado.");
        }
        return new StatusPedidoResponse(
            pedido.getId(),
            pedido.getStatus(),
            pedido.getValorCobrado(),
            pedido.getDesconto(),
            pedido.getDataHoraPagamento()
        );
    }
}
