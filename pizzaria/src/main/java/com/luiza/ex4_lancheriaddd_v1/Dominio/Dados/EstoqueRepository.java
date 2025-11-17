package com.luiza.ex4_lancheriaddd_v1.Dominio.Dados;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Ingrediente;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.ItemEstoque;

public interface EstoqueRepository {
    // Retorna um item de estoque pelo ingrediente
    ItemEstoque findByIngrediente(Ingrediente ingrediente);

    // Salva ou atualiza um item no estoque
    void save(ItemEstoque itemEstoque);
}
