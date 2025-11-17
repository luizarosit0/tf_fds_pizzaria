package com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.beans.factory.annotation.Autowired; // IMPORTAR
import org.springframework.stereotype.Service;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Dados.EstoqueRepository;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Ingrediente;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.ItemEstoque;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
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

            int quantidadePedida = itemPedido.getQuantidade();

            for (Ingrediente ingredienteDaReceita : itemPedido.getItem().getReceita().getIngredientes()) {

                ItemEstoque itemEmEstoque = estoqueRepository.findByIngrediente(ingredienteDaReceita);

                if (itemEmEstoque == null) {
                    System.err.println("Estoque: Ingrediente " + ingredienteDaReceita.getDescricao() + " não cadastrado.");
                    return false; 
                }

                if (itemEmEstoque.getQuantidade() < quantidadePedida) {
                    System.err.println("Estoque: Insuficiente para " + ingredienteDaReceita.getDescricao());
                    return false; 
                }
            }
        }
        return true; 
    }
}