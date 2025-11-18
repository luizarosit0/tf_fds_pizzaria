package com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades;

import java.util.List;

public class Receita {
    private long id;
    private String titulo;
    private List<ItemReceita> composicao; // agora Receita recebe List<ItemReceita>, ao inves de  List<Ingrediente> 

    public Receita(long id, String titulo, List<ItemReceita> composicao) {
        this.id = id;
        this.titulo = titulo;
        this.composicao = composicao;
    }

    public long getId() { return id; }
    public String getTitulo(){ return titulo; }
    
    public List<ItemReceita> getComposicao() { 
        return composicao; 
    }
}

