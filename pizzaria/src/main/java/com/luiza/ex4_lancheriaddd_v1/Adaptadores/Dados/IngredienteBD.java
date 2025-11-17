package com.luiza.ex4_lancheriaddd_v1.Adaptadores.Dados;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Ingrediente;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ingredientes")
public class IngredienteBD {
    @Id
    private long id;
    private String descricao;

    protected IngredienteBD() {} // Para o JPA

    public IngredienteBD(long id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    // converte de BD para Domínio
    public static Ingrediente fromIngredienteBD(IngredienteBD ingredienteBD) {
        return new Ingrediente(ingredienteBD.id, ingredienteBD.descricao);
    }
}