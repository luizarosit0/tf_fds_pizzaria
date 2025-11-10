package com.luiza.ex4_lancheriaddd_v1.Dominio.Dados;

import java.time.LocalDateTime;
import java.util.List;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido.Status;

public interface PedidoRepository {
    Pedido salvar(Pedido pedido);
    Pedido buscarPorId(long id);
    void atualizar(Pedido pedido);
    int quantPedidos(String CPF, int dias);
    List<Pedido> buscarPorStatusEPeriodo(Status status, LocalDateTime dataInicial, LocalDateTime dataFinal);
    double totalGastoUltimosDias(String CPF, int dias);
}
