package com.luiza.ex4_lancheriaddd_v1.Aplicacao;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.luiza.ex4_lancheriaddd_v1.Aplicacao.Responses.SubmeterPedidoResponse;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Dados.ClienteRepository;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Dados.ProdutosRepository;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos.PedidoService;

@Component
public class SubmeterPedidoUC {

    public record ItemData(long produtoId, int quantidade) {}

    private PedidoService pedidoService;
    private ClienteRepository clienteRepository;
    private ProdutosRepository produtosRepository; 

    @Autowired
    public SubmeterPedidoUC(PedidoService pedidoService, ClienteRepository clienteRepository, ProdutosRepository produtosRepository) {
        this.pedidoService = pedidoService;
        this.clienteRepository = clienteRepository;
        this.produtosRepository = produtosRepository;
    }

    public SubmeterPedidoResponse run(String clienteCpf, List<ItemData> itensData) {
        Cliente cliente = clienteRepository.buscarPorCpf(clienteCpf);

        if (cliente == null) {
            throw new IllegalArgumentException("Cliente com CPF " + clienteCpf + " não encontrado.");
        }

        List<ItemPedido> itens = itensData.stream()
            .map(itemData -> {
                Produto produto = produtosRepository.recuperaProdutoPorid(itemData.produtoId());
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
