package com.luiza.ex4_lancheriaddd_v1.Adaptadores.Dados;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Ingrediente;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.ItemEstoque;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "itensEstoque") 
public class ItemEstoqueBD {

    @Id
    private long id;

    @OneToOne // relacionamento 1:1
    @JoinColumn(name = "ingrediente_id")
    private IngredienteBD ingrediente;

    private int quantidade;

    protected ItemEstoqueBD() {}

    // converte de BD para Domínio
    public static ItemEstoque fromItemEstoqueBD(ItemEstoqueBD itemBD) {
        Ingrediente domainIngrediente = IngredienteBD.fromIngredienteBD(itemBD.ingrediente);
        return new ItemEstoque(domainIngrediente, itemBD.quantidade);
    }

    // converte de Domínio para BD
    public static ItemEstoqueBD toItemEstoqueBD(ItemEstoque item) {
        ItemEstoqueBD itemBD = new ItemEstoqueBD();
        itemBD.id = item.getIngrediente().getId(); 
        itemBD.ingrediente = new IngredienteBD(item.getIngrediente().getId(), item.getIngrediente().getDescricao());
        itemBD.quantidade = item.getQuantidade();
        return itemBD;
    }
}
