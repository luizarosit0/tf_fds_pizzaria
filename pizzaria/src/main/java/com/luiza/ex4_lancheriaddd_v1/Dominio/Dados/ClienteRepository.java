package com.luiza.ex4_lancheriaddd_v1.Dominio.Dados;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;

public interface ClienteRepository {
    Cliente buscarPorCpf(String cpf);
}
