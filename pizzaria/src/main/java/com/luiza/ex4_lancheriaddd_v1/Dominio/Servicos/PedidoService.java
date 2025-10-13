package com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido.Status;

//Responsável por verificar a consistência do pedido, 
//calcular valores, recuperar histórico de pedidos etc
@Service
public class PedidoService{
    private PedidoRepository pedidoRepository;
    private EstoqueServiceI estoqueServiceI;
    private ImpostoServiceI impostoServiceI;
    private DescontoServiceI descontoServiceI;

    @Autowired 
    public PedidoService(PedidoRepository pedidoRepository,
                         EstoqueServiceI estoqueServiceI,
                         ImpostoServiceI impostoServiceI,
                         DescontoServiceI descontoServiceI){
        this.pedidoRepository = pedidoRepository;
        this.estoqueServiceI = estoqueServiceI;
        this.impostoServiceI = impostoServiceI;
        this.descontoServiceI = descontoServiceI;
    }

    // UC2
    public Pedido submeter(Pedido pedido){
        // verificar o estoque 
        if(!estoqueServiceI.temEstoque(pedido)){
            pedido.setStatus(Status.CANCELADO);
            return pedido;
        }

        // calcular o valor bruto
        double valorBruto = calculaBruto(pedido);
        pedido.setValor(valorBruto); // atualizar o preço do pedido
        // desconto atribuido ao cliente 
        double desconto = descontoServiceI.calculaDesconto(pedido);
        pedido.setDesconto(desconto); 
        // atribuir os impostos em cima da diferença do bruto - desconto
        double base = valorBruto - desconto;
        double imposto = impostoServiceI.calculaImposto(base);
        pedido.setImpostos(imposto);
        // valor final
        double cobrado = valorBruto - desconto + imposto;
        pedido.setValorCobrado(cobrado);

        pedido.setStatus(Status.APROVADO);
        return pedidoRepository.salvar(pedido);
    }

    // valor bruto = somatorio dos preços de todos os itens
    private double calculaBruto(Pedido pedido) {
        return pedido.getItens().stream()
                .mapToDouble(item -> item.getItem().getPreco() * item.getQuantidade())
                .sum();
    }

}

