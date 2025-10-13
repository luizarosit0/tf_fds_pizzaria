package com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos;

//O serviço de estoque pode ser um “fake” que responde sempre que o 
//estoque é suficiente não importando o ingrediente.
public class EstoqueService {
    public boolean verificarDisponibilidade() {
        // Versão simplificada: sempre retorna que há estoque suficiente
        return true;
    }
}
