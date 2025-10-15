package com.luiza.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.Presenters;

import java.util.List;
import java.util.stream.Collectors;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

public class PedidoPresenter {

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

    public PedidoPresenter(Pedido pedido) {
        this.id = pedido.getId();
        this.status = pedido.getStatus().name(); // Converte o enum para String
        this.nomeCliente = pedido.getCliente().getNome();
        this.valorBruto = pedido.getValor();
        this.desconto = pedido.getDesconto();
        this.impostos = pedido.getImpostos();
        this.valorCobrado = pedido.getValorCobrado();
        
        this.itens = pedido.getItens().stream()
            .map(item -> new ItemPedidoPresenter(
                item.getItem().getDescricao(),
                item.getQuantidade(),
                item.getItem().getPreco() / 100.0 
            ))
            .collect(Collectors.toList());
    }

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
