package com.luiza.ex4_lancheriaddd_v1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos.ImpostoPadrao;

public class ImpostoPadraoTests {

    /*
    * Caso de Teste 1: Cálculo com valor positivo
    * Entrada: Valor base de 100.0
    * Resultado Esperado: 10.0 (10% de 100)
    */
    @Test 
    public void calculaDezPorcento() {
        ImpostoPadrao impostoPadrao = new ImpostoPadrao();
        double valorBase = 100.0;
        
        double resultado = impostoPadrao.calcular(valorBase);
        
        assertEquals(10.0, resultado, 0.001);
    }

    /*
    * Caso de Teste 2: Cálculo com valor zero
    * Entrada: Valor base de 0.0
    * Resultado Esperado: 0.0
    */
    @Test
    public void retornaZeroParaValorZero() {
        ImpostoPadrao impostoPadrao = new ImpostoPadrao();
        assertEquals(0.0, impostoPadrao.calcular(0.0), 0.001);
    }
}