package com.luiza.ex4_lancheriaddd_v1.Dominio.Dados;

import java.util.List;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;

public interface ProdutosRepository {
    Produto recuperaProdutoPorid(long id);
    List<Produto> recuperaProdutosCardapio(long id);
}
