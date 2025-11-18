package com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades;

public class ItemReceita {
    
    private Ingrediente ingrediente;
    private int quantidadeNecessaria; // quantidade necessaria para uma unidade de produto

    public ItemReceita(Ingrediente ingrediente, int quantidadeNecessaria) {
        // tratando exceçoes 
        if (ingrediente == null) throw new IllegalArgumentException("Ingrediente não pode ser nulo.");
        if (quantidadeNecessaria <= 0) throw new IllegalArgumentException("Quantidade necessária deve ser positiva.");

        this.ingrediente = ingrediente;
        this.quantidadeNecessaria = quantidadeNecessaria;
    }

    public Ingrediente getIngrediente() { 
        return ingrediente; 
    }
    
    public int getQuantidadeNecessaria() { 
        return quantidadeNecessaria; 
    }
}