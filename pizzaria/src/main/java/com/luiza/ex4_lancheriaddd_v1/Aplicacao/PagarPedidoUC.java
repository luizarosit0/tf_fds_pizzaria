package com.luiza.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.luiza.ex4_lancheriaddd_v1.Aplicacao.Responses.SubmeterPedidoResponse;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos.PedidoService;

@Component
public class PagarPedidoUC {
    private PedidoService pedidoService;

    @Autowired
    public PagarPedidoUC(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    // reutilizar o SubmeterPedidoResponse para evitar redundacias 
    public SubmeterPedidoResponse run(long idPedido) {
        
        Pedido pedidoPago = pedidoService.pagar(idPedido);
        
        return new SubmeterPedidoResponse(pedidoPago);
    }
}
