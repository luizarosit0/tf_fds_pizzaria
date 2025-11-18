package com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.stereotype.Service;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

@Service
public class DescontoNenhumService implements DescontoStrategy {

    @Override
    public double calcular(Pedido pedido) {
        return 0.0;
    }
}
