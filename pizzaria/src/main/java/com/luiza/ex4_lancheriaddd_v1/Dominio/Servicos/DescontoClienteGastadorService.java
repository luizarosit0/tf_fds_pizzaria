package com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

@Service
public class DescontoClienteGastadorService implements DescontoStrategyServiceI {

    private final PedidoRepository pedidoRepository;
    private double limite = 500.0;
    private int dias = 30;
    private double taxa = 0.15; // 15%

    @Autowired
    public DescontoClienteGastadorService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public double calcular(Pedido pedido) {
        String cpf = pedido.getCliente().getCpf();
        double totalGasto = pedidoRepository.totalGastoUltimosDias(cpf, dias);
        if (totalGasto > limite) {
            return pedido.getValor() * taxa;
        }
        return 0.0;
    }
}
