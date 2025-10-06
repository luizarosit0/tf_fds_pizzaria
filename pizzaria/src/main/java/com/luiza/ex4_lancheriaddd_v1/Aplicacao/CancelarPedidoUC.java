package com.luiza.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.luiza.ex4_lancheriaddd_v1.Aplicacao.Responses.CancelarResponse;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

@Component
public class CancelarPedidoUC {

    private final PedidoRepository pedidoRepo;

    @Autowired
    public CancelarPedidoUC(PedidoRepository pedidoRepo) {
        this.pedidoRepo = pedidoRepo;
    }

    public CancelarResponse run(long pedidoId) { // colocar isso no CancelarResponse
        Pedido pedido = pedidoRepo.buscarPorId(pedidoId);

        if (pedido == null) {
            return false;
        }

        //deve estar no status APROVADO para poder ser cancelado 
        if (pedido.getStatus() != Pedido.Status.APROVADO) {
            return false;
        }

        pedido.setStatus(Pedido.Status.CANCELADO);
        pedidoRepo.salvar(pedido); 

        return true;
    }
}