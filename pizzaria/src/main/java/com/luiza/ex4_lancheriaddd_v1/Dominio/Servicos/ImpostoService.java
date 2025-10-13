package com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos;

//Tanto o serviço de impostos como o serviço de descontos têm de ser 
//projetados de maneira a facilitar a mudança frequente nas fórmulas de 
//cálculo. 
public class ImpostoService {
    public double calcular(double base) {
        return base * 0.10; // 10% de imposto
    }
}
