package com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.stereotype.Service;

//projetados de maneira a facilitar a mudança frequente nas fórmulas de 
//cálculo. 
@Service
public class ImpostoService implements ImpostoServiceI{
    private double taxa = 0.10; // 10% em cima do somatorio dos preços

    @Override
    public double calculaImposto(double valor) {
        return valor * taxa; 
    }
}
