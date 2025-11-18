package com.luiza.ex4_lancheriaddd_v1;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.luiza.ex4_lancheriaddd_v1.Aplicacao.Responses.SubmeterPedidoResponse;
import com.luiza.ex4_lancheriaddd_v1.Aplicacao.SubmeterPedidoUC;
import com.luiza.ex4_lancheriaddd_v1.Aplicacao.SubmeterPedidoUC.ItemData;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido.Status;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Receita;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos.ClienteService;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos.PedidoService;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos.ProdutoService;

@ExtendWith(MockitoExtension.class)
public class SubmeterPedidoUCTests {

    @Mock 
    private PedidoService pedidoServiceMock;
    @Mock 
    private ClienteService clienteServiceMock;
    @Mock 
    private ProdutoService produtoServiceMock;

    private SubmeterPedidoUC submeterPedidoUC;

    @BeforeEach
    void setUp() {
        this.submeterPedidoUC = new SubmeterPedidoUC(
            pedidoServiceMock, 
            clienteServiceMock, 
            produtoServiceMock
        );
    }
    
    /*
     * Caso de Teste 1: Sucesso
     * Objetivo: Verificar se um pedido é criado e submetido com sucesso quando todos os dados são válidos.
     * Resultado Esperado: Retorna um Pedido com Status.APROVADO
     */
    @Test
    @DisplayName("Deve submeter pedido com sucesso quando dados são válidos")
    void submetePedidoComSucesso() {
        String cpf = "123";
        Cliente cliente = new Cliente(cpf, "Ana", "999", "Rua", "email");
        Receita receitaMock = mock(Receita.class);
        Produto produto = new Produto(1L, "Pizza", receitaMock, 5000);
        
        // simula que o cliente existe
        when(clienteServiceMock.buscarPorCpf(cpf)).thenReturn(cliente);
        // simula que o produto existe
        when(produtoServiceMock.recuperaProdutoPorid(1L)).thenReturn(produto);
        
        // simula o processamento do pedido
        Pedido pedidoProcessado = new Pedido(cliente, List.of());
        pedidoProcessado.setStatus(Status.APROVADO);
        when(pedidoServiceMock.submeter(any(Pedido.class))).thenReturn(pedidoProcessado); // O 'any' ignora o objeto exato

        List<ItemData> itens = List.of(new ItemData(1L, 2));
        SubmeterPedidoResponse response = submeterPedidoUC.run(cpf, itens);

        assertNotNull(response);
        assertEquals(Status.APROVADO, response.getPedido().getStatus());
    }

    /*
     * Caso de Teste 2: Cliente não encontrado
     * Objetivo: Verificar se o UC lança a exceção quando o CPF não existe
     * Resultado Esperado: Lançar IllegalArgumentException
     */
    @Test
    @DisplayName("Deve lançar erro se cliente não existir")
    void falhaSeClienteNaoExiste() {
        String cpfInvalido = "999";
        // simula que o cliente não existe
        when(clienteServiceMock.buscarPorCpf(cpfInvalido)).thenReturn(null);

        // verifica se a exceção é lançada durante a execução
        List<ItemData> itens = List.of(new ItemData(1L, 1));
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            submeterPedidoUC.run(cpfInvalido, itens);
        });

        assertEquals("Cliente com CPF " + cpfInvalido + " não encontrado.", exception.getMessage());
    }
    
    /*
     * Caso de Teste 3: Produto não encontrado
     * Objetivo: Verificar se o UC lança a exceção quando um ID de produto é inválido
     * Resultado Esperado: Lançar IllegalArgumentException
     */
    @Test
    @DisplayName("Deve lançar erro se um produto não existir")
    void falhaSeProdutoNaoExiste() {
        String cpfValido = "123";
        long idProdutoInvalido = 99L;
        
        // simula que o cliente existe
        when(clienteServiceMock.buscarPorCpf(cpfValido)).thenReturn(new Cliente(cpfValido, "", "", "", ""));
        // simula que o produto não existe
        when(produtoServiceMock.recuperaProdutoPorid(idProdutoInvalido)).thenReturn(null);

        List<ItemData> itens = List.of(new ItemData(idProdutoInvalido, 1));
        
        assertThrows(IllegalArgumentException.class, () -> {
            submeterPedidoUC.run(cpfValido, itens);
        });
    }
}
