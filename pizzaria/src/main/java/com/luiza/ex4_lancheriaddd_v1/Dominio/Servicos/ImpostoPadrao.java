package com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.stereotype.Component;

@Component
public class ImpostoPadrao implements ImpostoStrategy {
    
    private final double taxa = 0.10; // 10%

    @Override
    public double calcular(double valorBase) {
        return valorBase * taxa;
    }
}