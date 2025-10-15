package com.luiza.ex4_lancheriaddd_v1.Aplicacao;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.luiza.ex4_lancheriaddd_v1.Aplicacao.Responses.SubmeterPedidoResponse;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos.PedidoService;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos.ClienteService;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos.ProdutoService;

@Component
public class SubmeterPedidoUC {

    public record ItemData(long produtoId, int quantidade) {}

    private PedidoService pedidoService;
    private ClienteService clienteService;
    private ProdutoService produtoService; 

    @Autowired
    public SubmeterPedidoUC(PedidoService pedidoService, ClienteService clienteService, ProdutoService produtoService) {
        this.pedidoService = pedidoService;
        this.clienteService = clienteService;
        this.produtoService = produtoService;
    }

    public SubmeterPedidoResponse run(String clienteCpf, List<ItemData> itensData) {
        Cliente cliente = clienteService.buscarPorCpf(clienteCpf);

        if (cliente == null) {
            throw new IllegalArgumentException("Cliente com CPF " + clienteCpf + " não encontrado.");
        }

        List<ItemPedido> itens = itensData.stream()
            .map(itemData -> {
                Produto produto = produtoService.recuperaProdutoPorid(itemData.produtoId());
                if (produto == null) {
                    throw new IllegalArgumentException("Produto com ID " + itemData.produtoId() + " não encontrado.");
                }
                return new ItemPedido(produto, itemData.quantidade());
            })
            .collect(Collectors.toList());

        // criar o objeto de domínio Pedido no seu estado inicial
        Pedido novoPedido = new Pedido(cliente, itens);

        // chamar o serviço de domínio para aplicar as regras de negócio
        Pedido pedidoProcessado = pedidoService.submeter(novoPedido);

        return new SubmeterPedidoResponse(pedidoProcessado);
    }
}
