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

import com.luiza.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos.DescontoClienteFrequenteService;

@ExtendWith(MockitoExtension.class)
public class DescontoClienteFrequenteTests {
    
    @Mock 
    private PedidoRepository pedidoRepositoryMock;
    
    private DescontoClienteFrequenteService estrategia;

    @BeforeEach
    void setUp() {
        estrategia = new DescontoClienteFrequenteService(pedidoRepositoryMock);
    }
    
    // criar um pedido mockado 
    private Pedido criarPedidoFake(double valor) {
        Pedido p = mock(Pedido.class);
        Cliente c = new Cliente("123", "Teste", "999", "Rua", "email");
        when(p.getCliente()).thenReturn(c);
        when(p.getValor()).thenReturn(valor);
        return p;
    }

    /*
     * Caso de Teste 1: Cliente elegível (mais de 3 pedidos recentes)
     * Entrada: Pedido de R$ 100.00, Cliente tem 4 pedidos recentes.
     * Resultado esperado: R$ 7.00 -> 7% de desconto
     */
    @Test
    void aplicaDescontoSeFrequente() {
        
        Pedido pedido = criarPedidoFake(100.0);
        // quando o repositório for chamado, retorna 4
        when(pedidoRepositoryMock.quantPedidos(anyString(), anyInt())).thenReturn(4);

        double desconto = estrategia.calcular(pedido);
        
        assertEquals(7.0, desconto, 0.001);
    }

    /*
     * Caso de Teste 2: Cliente não elegível (3 ou menos pedidos recentes)
     * Entrada: Pedido de R$ 100.00, Cliente tem 2 pedidos recentes.
     * Resultado Esperado: R$ 0.00 -> não aplica desconto
     */
    @Test
    void naoAplicaDescontoSeNaoFrequente() {
        
        Pedido pedido = criarPedidoFake(100.0);
        // quando o repositório for chamado, retorna 2
        when(pedidoRepositoryMock.quantPedidos(anyString(), anyInt())).thenReturn(2);

        double desconto = estrategia.calcular(pedido);
        
        assertEquals(0.0, desconto, 0.001);
    }
}
