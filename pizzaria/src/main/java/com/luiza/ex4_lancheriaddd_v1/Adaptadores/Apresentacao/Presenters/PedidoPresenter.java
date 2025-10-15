package com.luiza.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.Presenters;

import java.util.List;
import java.util.stream.Collectors;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

public class PedidoPresenter {

    // Sub-classe interna para representar os itens de forma simples
    public record ItemPedidoPresenter(String descricaoProduto,
                                      int quantidade, 
                                      double precoUnitario) {}

    private long id;
    private String status;
    private String nomeCliente;
    private List<ItemPedidoPresenter> itens;
    private double valorBruto;
    private double desconto;
    private double impostos;
    private double valorCobrado;

    // Construtor que faz a transformação da Entidade para o Presenter
    public PedidoPresenter(Pedido pedido) {
        this.id = pedido.getId();
        this.status = pedido.getStatus().name(); // Converte o enum para String
        this.nomeCliente = pedido.getCliente().getNome();
        this.valorBruto = pedido.getValor();
        this.desconto = pedido.getDesconto();
        this.impostos = pedido.getImpostos();
        this.valorCobrado = pedido.getValorCobrado();
        
        // Transforma a lista de ItemPedido (entidade) para ItemPedidoPresenter (DTO)
        this.itens = pedido.getItens().stream()
            .map(item -> new ItemPedidoPresenter(
                item.getItem().getDescricao(),
                item.getQuantidade(),
                item.getItem().getPreco() / 100.0 // Exemplo de formatação
            ))
            .collect(Collectors.toList());
    }

    // Getters para que o Spring possa serializar para JSON
    public long getId() { 
        return id; 
    }
    public String getStatus() { 
        return status; 
    }
    public String getNomeCliente() { 
        return nomeCliente; 
    }
    public List<ItemPedidoPresenter> getItens() { 
        return itens; 
    }
    public double getValorBruto() { 
        return valorBruto; 
    }
    public double getDesconto() { 
        return desconto; 
    }
    public double getImpostos() { 
        return impostos; 
    }
    public double getValorCobrado() { 
        return valorCobrado; 
    }
}
