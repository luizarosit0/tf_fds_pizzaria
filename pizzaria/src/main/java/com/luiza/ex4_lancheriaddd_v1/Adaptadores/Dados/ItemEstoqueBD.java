package com.luiza.ex4_lancheriaddd_v1.Adaptadores.Dados;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Ingrediente;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.ItemEstoque;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "itensEstoque") 
public class ItemEstoqueBD {

    @Id
    private long id;
    
    @OneToOne // relacionamento 1:1
    private IngredienteBD ingrediente;
    private int quantidade;

    protected ItemEstoqueBD() {}

    public ItemEstoqueBD(long id, IngredienteBD ingrediente, int quantidade){
        this.id = id;
        this.ingrediente = ingrediente;
        this.quantidade = quantidade;
    }

    public long getId(){return id;}
    public IngredienteBD getIngrediente(){return ingrediente;}
    public int getQuantidade(){return quantidade;} 

    // converte de BD para Domínio
    public static ItemEstoque fromItemEstoqueBD(ItemEstoqueBD itemBD) {
        Ingrediente toIngrediente = IngredienteBD.fromIngredienteBD(itemBD.getIngrediente()); // converter o ingrediente
        return new ItemEstoque(toIngrediente, itemBD.getQuantidade());
    }

    // converte de Domínio para BD
    public static ItemEstoqueBD toItemEstoqueBD(ItemEstoque item) {
        long id = item.getIngrediente().getId();         
        IngredienteBD ingredienteBD = new IngredienteBD(id, item.getIngrediente().getDescricao());
        int quantidade = item.getQuantidade();

        return new ItemEstoqueBD(id, ingredienteBD, quantidade);
    }
}
