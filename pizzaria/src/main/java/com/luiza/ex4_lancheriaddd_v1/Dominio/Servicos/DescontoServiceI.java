package com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

public interface DescontoServiceI {
    double calculaDesconto(Pedido pedido);
}
