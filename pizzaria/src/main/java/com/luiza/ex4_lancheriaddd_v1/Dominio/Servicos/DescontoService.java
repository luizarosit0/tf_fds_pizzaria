package com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.TipoDesconto;

@Service
public class DescontoService implements DescontoServiceI {

    private PedidoRepository pedidoRepository;

    // ClienteFrequente
    private int minPedidos = 3; 
    private int diasFrequente = 20; 
    private double taxaFrequente = 0.07; // 7%

    // ClienteGastador
    private double limiteGastador = 500.0; 
    private int diasGastador = 30; 
    private double taxaGastador = 0.15; // 15%

    // tipo de desconto atualmente ativo (usuário "master")
    private TipoDesconto tipoDescontoAtivo = TipoDesconto.CLIENTE_FREQUENTE;

    @Autowired
    public DescontoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    // usuário master defina qual desconto está ativo no sistema
    public void definirTipoDescontoAtivo(TipoDesconto tipo) {
        this.tipoDescontoAtivo = tipo;
        System.out.println("Tipo de desconto ativo alterado para: " + tipo);
    }

    // retorna o tipo de desconto ativo no momento.
    public TipoDesconto getTipoDescontoAtivo() {
        return this.tipoDescontoAtivo;
    }

    @Override
    public double calculaDesconto(Pedido pedido) {
        String cpf = pedido.getCliente().getCpf();
        double subtotal = pedido.getValor();

        switch (tipoDescontoAtivo) {
            case CLIENTE_FREQUENTE:
                int pedidosRecentes = pedidoRepository.quantPedidos(cpf, diasFrequente);
                if (pedidosRecentes > minPedidos) {
                    pedido.setDesconto(subtotal * taxaFrequente);
                    System.out.println("Desconto aplicado: ClienteFrequente (7%)");
                    return subtotal * taxaFrequente;
                }
                break;

            case CLIENTE_GASTADOR:
                double totalGasto = pedidoRepository.totalGastoUltimosDias(cpf, diasGastador);
                if (totalGasto > limiteGastador) {
                    pedido.setDesconto(subtotal * taxaGastador);
                    System.out.println("Desconto aplicado: ClienteGastador (15%)");
                    return subtotal * taxaGastador;
                }
                break;

            default:
                break;
        }

        pedido.setDesconto(0.0);
        System.out.println("Sem desconto aplicado");
        return 0.0;
    }

    public TipoDesconto identificarTipoDesconto(Pedido pedido) {
        String cpf = pedido.getCliente().getCpf();

        int pedidosRecentes = pedidoRepository.quantPedidos(cpf, diasFrequente);
        double total30dias = pedidoRepository.totalGastoUltimosDias(cpf, diasGastador);

        if (total30dias > limiteGastador) {
            return TipoDesconto.CLIENTE_GASTADOR;
        } else if (pedidosRecentes > minPedidos) {
            return TipoDesconto.CLIENTE_FREQUENTE;
        } else {
            return TipoDesconto.NENHUM;
        }
    }
}

