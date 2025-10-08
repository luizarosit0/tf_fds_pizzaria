package com.luiza.ex4_lancheriaddd_v1.Aplicacao;

import com.luiza.ex4_lancheriaddd_v1.Aplicacao.Responses.SubmeterPedidoResponse;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos.DescontoService;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos.EstoqueService;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos.ImpostoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SubmeterPedidoUC {

    private final PedidoRepository pedidoRepo;
    private final EstoqueService estoqueService;
    private final DescontoService descontoService;
    private final ImpostoService impostoService;

    @Autowired
    public SubmeterPedidoUC(PedidoRepository pedidoRepo,
                            EstoqueService estoqueService,
                            DescontoService descontoService,
                            ImpostoService impostoService) {
        this.pedidoRepo = pedidoRepo;
        this.estoqueService = estoqueService;
        this.descontoService = descontoService;
        this.impostoService = impostoService;
    }

    public SubmeterPedidoResponse run(Pedido pedido, int pedidosRecentes) {
        // O objeto Pedido já vem pré-montado da requisição
        pedido.setStatus(Pedido.Status.NOVO);

        if (!estoqueService.verificarDisponibilidade()) {
            pedido.setStatus(Pedido.Status.REPROVADO);
            return new SubmeterPedidoResponse(pedido, false, "Ingredientes insuficientes.");
        }

        double subtotal = pedido.getItens().stream()
                .mapToDouble(item -> item.getPrecoUnitario() * item.getQuantidade())
                .sum();

        double desconto = descontoService.calcular(subtotal, pedidosRecentes);
        double imposto = impostoService.calcular(subtotal);
        double total = subtotal - desconto + imposto;

        pedido.setStatus(Pedido.Status.APROVADO);
        pedido.setValores(subtotal, imposto, desconto, total);
        
        Pedido pedidoSalvo = pedidoRepo.salvar(pedido);

        return new SubmeterPedidoResponse(pedidoSalvo, true, "Pedido aprovado com sucesso!");
    }
}