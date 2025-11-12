package com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

@Service
public class  DescontoClienteFrequenteService implements DescontoStrategyServiceI {

    private final PedidoRepository pedidoRepository;
    private final int minPedidos = 3;
    private final int diasFrequente = 20;
    private final double taxa = 0.07; // 7% 

    @Autowired
    public  DescontoClienteFrequenteService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public double calcular(Pedido pedido) {
        String cpf = pedido.getCliente().getCpf();
        int pedidosRecentes = pedidoRepository.quantPedidos(cpf, diasFrequente);
        if (pedidosRecentes > minPedidos) {
            return pedido.getValor() * taxa;
        }
        return 0.0;
    }
}
