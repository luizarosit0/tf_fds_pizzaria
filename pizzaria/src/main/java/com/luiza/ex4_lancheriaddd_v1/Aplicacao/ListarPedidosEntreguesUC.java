package com.luiza.ex4_lancheriaddd_v1.Aplicacao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.luiza.ex4_lancheriaddd_v1.Aplicacao.Responses.ListarPedidosEntreguesResponse; 
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos.PedidoService;

@Component
public class ListarPedidosEntreguesUC {
    private final PedidoService pedidoService;

    @Autowired
    public ListarPedidosEntreguesUC(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    public ListarPedidosEntreguesResponse run(LocalDateTime dataInicial, LocalDateTime dataFinal) {
        if (dataInicial == null || dataFinal == null) {
             throw new IllegalArgumentException("Datas inicial e final inválidas.");
        }
        
        List<Pedido> pedidos = pedidoService.buscarEntreguesPorPeriodo(Pedido.Status.ENTREGUE, dataInicial, dataFinal);
        
        return new ListarPedidosEntreguesResponse(pedidos);
    }
}
