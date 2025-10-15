package com.luiza.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.luiza.ex4_lancheriaddd_v1.Aplicacao.Responses.StatusPedidoResponse;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos.PedidoService;

@Component
public class SolicitarStatusUC {
    private  PedidoService pedidoService;

    @Autowired
    public SolicitarStatusUC(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    public StatusPedidoResponse run(long idPedido) {
        Pedido pedido = pedidoService.buscarPorId(idPedido);
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
