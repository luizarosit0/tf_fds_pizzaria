package com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos;

public class DescontoService {
    // Aplica 7% se cliente tiver mais de 3 pedidos nos últimos 20 dias
    public double calcular(double subtotal, int pedidosRecentes) {
        if (pedidosRecentes > 3) {
            return subtotal * 0.07;
        }
        return 0.0;
    }
}
