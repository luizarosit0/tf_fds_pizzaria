package com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

@Service
public class CozinhaService {
    private Queue<Pedido> filaEntrada;
    private Pedido emPreparacao;
    private Queue<Pedido> filaSaida;

    private ScheduledExecutorService scheduler; // para simular o tempo de preparo
    
    private PedidoRepository pedidoRepository;
    private EntregaServiceI entregaService; 

    @Autowired
    public CozinhaService(PedidoRepository pedidoRepository, EntregaServiceI entregaService) {
        this.pedidoRepository = pedidoRepository;
        this.entregaService = entregaService;
        filaEntrada = new LinkedBlockingQueue<Pedido>();
        emPreparacao = null;
        filaSaida = new LinkedBlockingQueue<Pedido>();
        scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    private synchronized void colocaEmPreparacao(Pedido pedido){
        pedido.setStatus(Pedido.Status.PREPARACAO); // atualiza status 
        pedidoRepository.atualizar(pedido); // atualiza o pedido

        emPreparacao = pedido; 
        System.out.println("Pedido em preparacao: "+pedido);
        // Agenda pedidoPronto para ser chamado em 2 segundos
        scheduler.schedule(() -> pedidoPronto(), 5, TimeUnit.SECONDS); // o pedido fica pronto em 5s
    }

    public synchronized void chegadaDePedido(Pedido p) { // ele vem como PAGO ja
        filaEntrada.add(p); // pedido na fila de preparo
        System.out.println("Pedido na fila de entrada: "+p);
        
        if (emPreparacao == null) {
            colocaEmPreparacao(filaEntrada.poll()); // manda preparar 
        }
    }

    public synchronized void pedidoPronto() {
        emPreparacao.setStatus(Pedido.Status.PRONTO); // atualiza status
        pedidoRepository.atualizar(emPreparacao);

        filaSaida.add(emPreparacao);
        System.out.println("Pedido na fila de saida: "+emPreparacao);

        //depois de pronto, eh encaminhado para a entrega
        entregaService.chegadaDePedido(emPreparacao);
        
        emPreparacao = null; //acabou a preparacao
        // Se tem pedidos na fila, programa a preparação para daqui a 1 segundo
        if (!filaEntrada.isEmpty()){
            Pedido prox = filaEntrada.poll();
            scheduler.schedule(() -> colocaEmPreparacao(prox), 1, TimeUnit.SECONDS);
        }
    }
}
