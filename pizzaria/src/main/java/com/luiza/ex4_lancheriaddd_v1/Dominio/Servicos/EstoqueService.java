package com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.stereotype.Service;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
//O serviço de estoque pode ser um “fake” que responde sempre que o 
//estoque é suficiente não importando o ingrediente.
@Service
public class EstoqueService implements EstoqueServiceI {

    @Override
    public boolean temEstoque(Pedido pedido){
        return true;
    }
}
