package com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImpostoService implements ImpostoServiceI {

    private final ImpostoStrategy impostoStrategy;

    @Autowired
    public ImpostoService(ImpostoStrategy impostoStrategy) {
        this.impostoStrategy = impostoStrategy;
    }

    @Override
    public double calculaImposto(double valor) {
        return impostoStrategy.calcular(valor);
    }
}
