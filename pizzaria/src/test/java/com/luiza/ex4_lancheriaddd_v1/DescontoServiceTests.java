package com.luiza.ex4_lancheriaddd_v1;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.TipoDesconto;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos.DescontoClienteFrequenteService;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos.DescontoClienteGastadorService;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos.DescontoNenhumService;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos.DescontoService;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos.DescontoStrategy;

@ExtendWith(MockitoExtension.class)
public class DescontoServiceTests {
    
    @Mock 
    DescontoClienteFrequenteService frequenteMock;
    @Mock 
    DescontoClienteGastadorService gastadorMock;
    @Mock 
    DescontoNenhumService nenhumMock;
    
    private DescontoService descontoService;

    @BeforeEach
    void setUp() {
        // lista de mocks
        List<DescontoStrategy> estrategias = List.of(
            frequenteMock, 
            gastadorMock, 
            nenhumMock
        );
        descontoService = new DescontoService(estrategias);        
    }

    /*
     * Caso de Teste 1: Troca para estratégia Cliente Frequente
     * Objetivo: Ao definir 'CLIENTE_FREQUENTE', o serviço deve chamar o método calcular() 
     *           APENAS no mock Frequente
     */
    @Test
    void usaEstrategiaFrequenteQuandoAtiva() {
        Pedido pedido = mock(Pedido.class); // o que é isso
        when(frequenteMock.calcular(pedido)).thenReturn(7.0); // 7%
        
        descontoService.definirTipoDescontoAtivo(TipoDesconto.CLIENTE_FREQUENTE);
        double resultado = descontoService.calculaDesconto(pedido);

        assertEquals(7.0, resultado);
        verify(frequenteMock).calcular(pedido); 
        
        // garante que NÃO chamou nos outros mocks
        verify(gastadorMock, org.mockito.Mockito.never()).calcular(pedido); // o que é isso
        verify(nenhumMock, org.mockito.Mockito.never()).calcular(pedido);
    }
    
    /*
     * Caso de Teste 2: Troca para Estratégia Cliente Gastador
     * Objetivo: Ao definir 'CLIENTE_GASTADOR', o serviço deve chamar o método calcular() 
     *           APENAS no mock Gastador
     */
    @Test
    void deveUsarEstrategiaGastadorQuandoAtiva() {
        Pedido pedido = mock(Pedido.class);
        when(gastadorMock.calcular(pedido)).thenReturn(15.0); //15%
        
        descontoService.definirTipoDescontoAtivo(TipoDesconto.CLIENTE_GASTADOR);
        double resultado = descontoService.calculaDesconto(pedido);

        assertEquals(15.0, resultado);
        verify(gastadorMock).calcular(pedido); 

        // garante que NÃO chamou nos outros mocks
        verify(frequenteMock, org.mockito.Mockito.never()).calcular(pedido); 
        verify(nenhumMock, org.mockito.Mockito.never()).calcular(pedido);
    }
}
