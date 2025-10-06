package com.luiza.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.luiza.ex4_lancheriaddd_v1.Aplicacao.Responses.ConsultarStatusResponse;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

@Component
public class ConsultarStatusUC {

    private final PedidoRepository pedidoRepo;

    @Autowired
    public ConsultarStatusUC(PedidoRepository pedidoRepo) {
        this.pedidoRepo = pedidoRepo;
    }

    public ConsultarStatusResponse run(long pedidoId) {
        // A lógica é simplesmente buscar o pedido pelo ID.
        // A camada do Controller vai se encarregar de decidir o que fazer
        // se o pedido for nulo (não encontrado).
        return pedidoRepo.buscarPorId(pedidoId);
    }
}
