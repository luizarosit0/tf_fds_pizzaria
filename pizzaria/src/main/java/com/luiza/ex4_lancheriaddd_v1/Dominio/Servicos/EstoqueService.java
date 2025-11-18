package com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Dados.EstoqueRepository;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Ingrediente;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.ItemEstoque;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.ItemReceita; // import da nova classe 
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido; 

@Service
public class EstoqueService implements EstoqueServiceI {

    private final EstoqueRepository estoqueRepository;

    @Autowired
    public EstoqueService(EstoqueRepository estoqueRepository) {
        this.estoqueRepository = estoqueRepository;
    }

    @Override
    public boolean temEstoque(Pedido pedido) {

        for (ItemPedido itemPedido : pedido.getItens()) {

            int quantPedida = itemPedido.getQuantidade(); // quantidade de pizzas 

            // iterando sobre a composição da Receita -> lista de ItemReceita
            for (ItemReceita itemDaReceita : itemPedido.getItem().getReceita().getComposicao()) { 
                
                int quantNecessariaPorUnidade = itemDaReceita.getQuantidadeNecessaria(); 
                
                // consumo TOTAL = quantidade necessária * quantidade de pizzas
                int consumoTotal = quantNecessariaPorUnidade * quantPedida;

                Ingrediente ingrediente = itemDaReceita.getIngrediente();

                ItemEstoque itemEmEstoque = estoqueRepository.findByIngrediente(ingrediente);

                if (itemEmEstoque == null) {
                    System.err.println("Estoque: Ingrediente " + ingrediente.getDescricao() + " não cadastrado.");
                    return false; 
                }

                // compara estoque com o consumo total do ingrediente
                if (itemEmEstoque.getQuantidade() < consumoTotal) {
                    System.err.println("Estoque: Insuficiente para " + ingrediente.getDescricao());
                    return false; 
                }
            }
        }
        return true; 
    }
}