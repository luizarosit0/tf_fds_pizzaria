package com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido.Status;

//Responsável por verificar a consistência do pedido, 
//calcular valores, recuperar histórico de pedidos etc
@Service
public class PedidoService{
    private final PedidoRepository pedidoRepository;
    private final EstoqueServiceI estoqueServiceI;
    private final ImpostoServiceI impostoServiceI;
    private final DescontoServiceI descontoServiceI;
    private final PagamentoServiceI pagamentoService;
    private final CozinhaService cozinhaService;

    @Autowired 
    public PedidoService(PedidoRepository pedidoRepository,
                         EstoqueServiceI estoqueServiceI,
                         ImpostoServiceI impostoServiceI,
                         DescontoServiceI descontoServiceI,
                         PagamentoServiceI pagamentoService,
                         CozinhaService cozinhaService){
        this.pedidoRepository = pedidoRepository;
        this.estoqueServiceI = estoqueServiceI;
        this.impostoServiceI = impostoServiceI;
        this.descontoServiceI = descontoServiceI;
        this.pagamentoService = pagamentoService;
        this.cozinhaService = cozinhaService;
    }

    public Pedido salvar(Pedido pedido){
        return pedidoRepository.salvar(pedido);
    }
    public Pedido buscarPorId(long idPedido) {
        return pedidoRepository.buscarPorId(idPedido);
    }

    public void atualizarStatus(Pedido pedido) {
        pedidoRepository.atualizar(pedido);
    }

    public int quantPedidos(String CPF, int dias) {
        return pedidoRepository.quantPedidos(CPF, dias);
    }

    public List<Pedido> buscarEntreguesPorPeriodo(Status status, LocalDateTime dataInicial, LocalDateTime dataFinal) {
        return pedidoRepository.buscarPorStatusEPeriodo(status, dataInicial, dataFinal);
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

    //UC5
    public Pedido pagar(long idPedido) {
        Pedido pedido = this.buscarPorId(idPedido);
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido com ID " + idPedido + " não encontrado.");
        }
        
        //so se for aprovado
        if (pedido.getStatus() != Status.APROVADO) {
            throw new IllegalStateException("O pedido não foi aprovado para pagamento.");
        }
        
        // processa o pagamento
        boolean pagamentoOk = pagamentoService.processaPagamento(pedido.getValorCobrado());
        
        if (pagamentoOk) {
            // atualiza o status e a data
            pedido.setStatus(Status.PAGO);
            pedido.setDataHoraPagamento(LocalDateTime.now());
            
            pedidoRepository.atualizar(pedido);
            
            // pedido vai para a fila da cozinha
            cozinhaService.chegadaDePedido(pedido);
            
            return pedido;
        } else {
            // se o pagamento falhar
            throw new RuntimeException("Falha ao processar o pagamento.");
        }
    }

    // valor bruto = somatorio dos preços de todos os itens
    private double calculaBruto(Pedido pedido) {
        return pedido.getItens().stream()
                .mapToDouble(item -> item.getItem().getPreco() * item.getQuantidade())
                .sum();
    }

}
