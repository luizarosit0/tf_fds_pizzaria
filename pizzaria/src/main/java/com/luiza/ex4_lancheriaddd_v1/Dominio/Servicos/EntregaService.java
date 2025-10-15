package com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos;

//Mantém a fila de entregas, atribui entregadores e acompanha a entrega
//O serviço de entregas pode ser simulado da mesma forma como o serviço 
//de cozinha, inclusive no que diz respeito a atualização do status do pedido 
//no banco de dados. 
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido.Status;

@Service
public class EntregaService implements EntregaServiceI {
    private Queue<Pedido> filaEntrada;
    private Pedido emTransporte; // simula o entregador
    
    private ScheduledExecutorService scheduler;
    private PedidoRepository pedidoRepository;

    @Autowired
    public EntregaService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
        filaEntrada = new LinkedBlockingQueue<Pedido>();
        emTransporte = null;
        scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    private synchronized void colocaEmTransporte(Pedido pedido) {
        pedido.setStatus(Status.TRANSPORTE);
        pedidoRepository.atualizar(pedido); 
        emTransporte = pedido;
        System.out.println("Pedido em transporte: " + pedido.getId());

        // simula o tempo de entrega em 15s -> para testar os status 
        scheduler.schedule(() -> pedidoEntregue(), 15, TimeUnit.SECONDS); 
    }

    @Override
    public synchronized void chegadaDePedido(Pedido p) {
        p.setStatus(Status.AGUARDANDO);
        pedidoRepository.atualizar(p); 
        filaEntrada.add(p);
        System.out.println("Pedido aguardando na fila de entrega: " + p.getId());

        if (emTransporte == null) {
            colocaEmTransporte(filaEntrada.poll());
        }
    }

    private synchronized void pedidoEntregue() {
        if (emTransporte != null) {
            emTransporte.setStatus(Status.ENTREGUE);
            pedidoRepository.atualizar(emTransporte); 
            System.out.println("Pedido ENTREGUE: " + emTransporte.getId());
            
            emTransporte = null; // entregador livre
            
            if (!filaEntrada.isEmpty()) {
                Pedido prox = filaEntrada.poll();
                scheduler.schedule(() -> colocaEmTransporte(prox), 1, TimeUnit.SECONDS);
            }
        }
    }
}
