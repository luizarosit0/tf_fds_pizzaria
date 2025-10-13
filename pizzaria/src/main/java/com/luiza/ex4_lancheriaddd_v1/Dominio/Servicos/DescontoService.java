package com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

//Tanto o serviço de impostos como o serviço de descontos têm de ser 
//projetados de maneira a facilitar a mudança frequente nas fórmulas de 
//cálculo. 
@Service
public class DescontoService implements DescontoServiceI{
    private PedidoRepository pedidoRepository; //os descontos precisam dos dados do pedido

    private int minPedidos = 3; 
    private int dias = 20; 
    private double taxa = 0.07; // 7% de desconto

    @Autowired
    public DescontoService(PedidoRepository pedidoRepository){
        this.pedidoRepository = pedidoRepository;
    }

    // Aplica 7% se cliente tiver mais de 3 pedidos nos últimos 20 dias
    public double calculaDesconto(Pedido pedido) {
        String CPF = pedido.getCliente().getCpf();

        int recentes = pedidoRepository.quantPedidos(CPF, dias);

        if (recentes > minPedidos) {
            double subtotal = pedido.getValor();
            return subtotal * taxa;
        }
        return 0.0;
    }
}
