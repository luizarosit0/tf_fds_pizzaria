package com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.TipoDesconto;

public interface DescontoServiceI {
    double calculaDesconto(Pedido pedido);
    void definirTipoDescontoAtivo(TipoDesconto tipo);
}
