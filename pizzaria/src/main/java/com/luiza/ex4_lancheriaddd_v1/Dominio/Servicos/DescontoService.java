package com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.TipoDesconto;

@Service
public class DescontoService implements DescontoServiceI {

    private final Map<TipoDesconto, DescontoStrategyServiceI> estrategias = new HashMap<>();
    private TipoDesconto tipoDescontoAtivo = TipoDesconto.CLIENTE_FREQUENTE;

    // lista de todas as classes que implementam DescontoStrategyServiceI
    @Autowired
    public DescontoService(List<DescontoStrategyServiceI> listaDeEstrategias) {

        // registrar estratégias disponíveis
        for (DescontoStrategyServiceI estrategia : listaDeEstrategias) {
            if (estrategia instanceof DescontoClienteFrequenteService) {
                estrategias.put(TipoDesconto.CLIENTE_FREQUENTE, estrategia);
            } else if (estrategia instanceof DescontoClienteGastadorService) {
                estrategias.put(TipoDesconto.CLIENTE_GASTADOR, estrategia);
            } else if (estrategia instanceof DescontoNenhumService) {
                estrategias.put(TipoDesconto.NENHUM, estrategia);
            }
        }
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

        DescontoStrategyServiceI estrategia = estrategias.getOrDefault(tipoDescontoAtivo, new DescontoNenhumService());
        double desconto = estrategia.calcular(pedido);

        if (desconto > 0)
            System.out.println("Desconto aplicado: " + tipoDescontoAtivo + " (" + desconto + ")");
        else
            System.out.println("Sem desconto aplicado.");

        return desconto;
    }
}
