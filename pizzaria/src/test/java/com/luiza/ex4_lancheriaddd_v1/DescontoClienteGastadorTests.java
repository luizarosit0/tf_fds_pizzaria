package com.luiza.ex4_lancheriaddd_v1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings; 
import org.mockito.quality.Strictness;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos.DescontoClienteGastadorService;

@MockitoSettings(strictness = Strictness.LENIENT) // mais tolerante
@ExtendWith(MockitoExtension.class)
public class DescontoClienteGastadorTests {
    
    @Mock 
    private PedidoRepository pedidoRepositoryMock;
    
    private DescontoClienteGastadorService strategy;

    @BeforeEach
    void setUp() {
        strategy = new DescontoClienteGastadorService(pedidoRepositoryMock);
    }

    private Pedido criarPedidoFake(double valor) {
        Pedido p = mock(Pedido.class);
        Cliente c = new Cliente("123", "Teste", "999", "Rua", "email");
        when(p.getCliente()).thenReturn(c);
        when(p.getValor()).thenReturn(valor);
        return p;
    }

    /*
     * Caso de Teste 1: Cliente elegível (Gasto > R$ 500)
     * Entrada: Pedido de R$ 100.00, gasto recente de R$ 600.00.
     * Resultado esperado: R$ 15.00 -> 15% de desconto
     */
    @Test
    void aplicaaDescontoSeGastador() {
        Pedido pedido = criarPedidoFake(100.0);
        when(pedidoRepositoryMock.totalGastoUltimosDias(anyString(), anyInt())).thenReturn(600.0);

        double desconto = strategy.calcular(pedido);
        
        assertEquals(15.0, desconto, 0.001);
    }
    
    /*
     * Caso de Teste 2: Cliente não elegível (Gasto <= R$ 500)
     * Entrada: Pedido de R$ 100.00, gasto recente de R$ 499.00.
     * Resultado Esperado: R$ 0.00 -> deconto não é aplicado
     */
    @Test
    void naoAplicaDescontoSeNaoGastador() {
        Pedido pedido = criarPedidoFake(100.0);
        when(pedidoRepositoryMock.totalGastoUltimosDias(anyString(), anyInt())).thenReturn(499.0);

        double desconto = strategy.calcular(pedido);
        
        assertEquals(0.0, desconto, 0.001);
    }
}
