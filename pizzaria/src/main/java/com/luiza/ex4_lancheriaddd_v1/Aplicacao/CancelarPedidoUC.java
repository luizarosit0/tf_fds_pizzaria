package com.luiza.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.luiza.ex4_lancheriaddd_v1.Aplicacao.Responses.SubmeterPedidoResponse;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos.PedidoService;

@Component 
public class CancelarPedidoUC {
    private PedidoService pedidoService;

    @Autowired
    public CancelarPedidoUC(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    // reutilizar o SubmeterPedidoResponse, pois soh queremos ver o pedido modificado
    // criar outro response tornaria redudndante
    public SubmeterPedidoResponse run(long idPedido) {
        Pedido pedido = pedidoService.buscarPorId(idPedido);
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido não encontrado.");
        }

        if (pedido.getStatus() != Pedido.Status.APROVADO) {
            throw new IllegalStateException("Somente pedidos aprovados e não pagos podem ser cancelados.");
        }

        // atualiza o status
        pedido.setStatus(Pedido.Status.CANCELADO);
        pedidoService.atualizarStatus(pedido);

        return new SubmeterPedidoResponse(pedido);
    }
}
