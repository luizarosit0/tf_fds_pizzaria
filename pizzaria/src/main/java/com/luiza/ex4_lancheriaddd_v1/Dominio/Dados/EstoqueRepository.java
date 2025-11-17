package com.luiza.ex4_lancheriaddd_v1.Dominio.Dados;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Ingrediente;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.ItemEstoque;

public interface EstoqueRepository {
    
    ItemEstoque findByIngrediente(Ingrediente ingrediente);
    void save(ItemEstoque itemEstoque);
}
