package com.luiza.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.luiza.ex4_lancheriaddd_v1.Aplicacao.Responses.CancelarPedidoResponse;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

@Component 
public class CancelarPedidoUC {
    private final PedidoRepository pedidoRepository;

    @Autowired
    public CancelarPedidoUC(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public CancelarPedidoResponse run(long idPedido) {
        Pedido pedido = pedidoRepository.buscarPorId(idPedido);
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido não encontrado.");
        }

        if (pedido.getStatus() != Pedido.Status.APROVADO) {
            throw new IllegalStateException("Somente pedidos aprovados e não pagos podem ser cancelados.");
        }

        // Atualiza o status
        pedido.setStatus(Pedido.Status.CANCELADO);
        pedidoRepository.atualizar(pedido);

        return new CancelarPedidoResponse(
            pedido.getId(),
            pedido.getStatus(),
            "Pedido cancelado com sucesso."
        );
    }
}
