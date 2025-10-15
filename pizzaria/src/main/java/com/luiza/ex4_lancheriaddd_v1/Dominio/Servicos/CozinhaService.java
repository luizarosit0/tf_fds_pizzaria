package com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

@Service
public class CozinhaService {
    private Queue<Pedido> filaEntrada;
    private Pedido emPreparacao;
    private Queue<Pedido> filaSaida;

    private ScheduledExecutorService scheduler; // para simular o tempo de preparo 

    public CozinhaService() {
        filaEntrada = new LinkedBlockingQueue<Pedido>();
        emPreparacao = null;
        filaSaida = new LinkedBlockingQueue<Pedido>();
        scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    private synchronized void colocaEmPreparacao(Pedido pedido){
        pedido.setStatus(Pedido.Status.PREPARACAO); // atualiza status 
        emPreparacao = pedido;
        System.out.println("Pedido em preparacao: "+pedido);
        // Agenda pedidoPronto para ser chamado em 2 segundos
        scheduler.schedule(() -> pedidoPronto(), 5, TimeUnit.SECONDS); // o pedido fica pronto em 5s
    }

    public synchronized void chegadaDePedido(Pedido p) {
        filaEntrada.add(p); // pedido na fila de preparo
        System.out.println("Pedido na fila de entrada: "+p);
        if (emPreparacao == null) {
            colocaEmPreparacao(filaEntrada.poll()); // manda preparar 
        }
    }

    public synchronized void pedidoPronto() {
        emPreparacao.setStatus(Pedido.Status.PRONTO); // atualiza status
        filaSaida.add(emPreparacao);
        System.out.println("Pedido na fila de saida: "+emPreparacao);
        emPreparacao = null; //acabou a preparacao
        // Se tem pedidos na fila, programa a preparação para daqui a 1 segundo
        if (!filaEntrada.isEmpty()){
            Pedido prox = filaEntrada.poll();
            scheduler.schedule(() -> colocaEmPreparacao(prox), 1, TimeUnit.SECONDS);
        }
    }
}
