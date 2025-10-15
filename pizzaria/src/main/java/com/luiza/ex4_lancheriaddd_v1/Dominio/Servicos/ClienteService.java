package com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Dados.ClienteRepository;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;

@Service
public class ClienteService {
    private ClienteRepository clienteRepository;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository){
        this.clienteRepository = clienteRepository;
    }

    public Cliente buscarPorCpf(String cpf){
        return clienteRepository.buscarPorCpf(cpf);
    }

    
}
