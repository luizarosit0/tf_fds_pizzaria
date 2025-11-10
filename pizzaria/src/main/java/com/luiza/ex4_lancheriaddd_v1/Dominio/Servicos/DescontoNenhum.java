package com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

public class DescontoNenhum implements DescontoStrategyServiceI {

    @Override
    public double calcular(Pedido pedido) {
        return 0.0;
    }
}

