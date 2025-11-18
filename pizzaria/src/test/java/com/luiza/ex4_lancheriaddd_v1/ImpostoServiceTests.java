package com.luiza.ex4_lancheriaddd_v1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos.ImpostoService;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos.ImpostoStrategy;

@ExtendWith(MockitoExtension.class) 
public class ImpostoServiceTests {

    @Mock 
    private ImpostoStrategy impostoStrategyMock; // dublê

    private ImpostoService impostoService;

    @BeforeEach //antes de cada teste
    void setUp() {
        this.impostoService = new ImpostoService(impostoStrategyMock); //injecao
    }

    /*
    * Caso de Teste: Aplicação correta
    * Objetivo: Verificar se o serviço aplica o cálculo para a estratégia injetada.
    * Entrada: Valor 200.0
    * Comportamento do Mock: Quando chamado calcular(200.0), retornar 20.0
    * Verificação: O serviço deve retornar 20.0 e o método do mock deve ter sido chamado.
    */
    @Test
    public void aplicaParaEstrategia() {
        double valorEntrada = 200.0;
        double valorEsperado = 20.0;
        when(impostoStrategyMock.calcular(valorEntrada)).thenReturn(valorEsperado);

        double resultado = impostoService.calculaImposto(valorEntrada);

        assertEquals(valorEsperado, resultado);
        
        verify(impostoStrategyMock).calcular(valorEntrada);
    }
}