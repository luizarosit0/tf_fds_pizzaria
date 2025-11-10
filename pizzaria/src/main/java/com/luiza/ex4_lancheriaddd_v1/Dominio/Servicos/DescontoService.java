package com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.TipoDesconto;

@Service
public class DescontoService implements DescontoServiceI {

    private final PedidoRepository pedidoRepository;
    private final Map<TipoDesconto, DescontoStrategyServiceI> estrategias = new HashMap<>();
    private TipoDesconto tipoDescontoAtivo = TipoDesconto.CLIENTE_FREQUENTE;

    @Autowired
    public DescontoService(PedidoRepository pedidoRepository) {
        
        this.pedidoRepository = pedidoRepository;

        // registrar estratégias disponíveis
        estrategias.put(TipoDesconto.CLIENTE_FREQUENTE, new DescontoClienteFrequenteService(pedidoRepository));
        estrategias.put(TipoDesconto.CLIENTE_GASTADOR, new DescontoClienteGastadorService(pedidoRepository));
        estrategias.put(TipoDesconto.NENHUM, new DescontoNenhum());
    }

    public void definirTipoDescontoAtivo(TipoDesconto tipo) {

        this.tipoDescontoAtivo = tipo;
        System.out.println("Tipo de desconto ativo alterado para: " + tipo);
    }

    public TipoDesconto getTipoDescontoAtivo() {

        return this.tipoDescontoAtivo;
    }

    @Override
    public double calculaDesconto(Pedido pedido) {

        DescontoStrategyServiceI estrategia = estrategias.getOrDefault(tipoDescontoAtivo, new DescontoNenhum());
        double desconto = estrategia.calcular(pedido);
        pedido.setDesconto(desconto);

        if (desconto > 0)
            System.out.println("Desconto aplicado: " + tipoDescontoAtivo + " (" + desconto + ")");
        else
            System.out.println("Sem desconto aplicado.");

        return desconto;
    }
}
