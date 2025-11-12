package com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Dados.ProdutosRepository;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;

@Service
public class ProdutoService {
    private final ProdutosRepository produtosRepository;

    @Autowired
    public ProdutoService(ProdutosRepository produtosRepository){
        this.produtosRepository = produtosRepository;
    }

    public Produto recuperaProdutoPorid(long id){
        return produtosRepository.recuperaProdutoPorid(id);
    }

    public List<Produto> recuperaProdutosCardapio(long id){
        return produtosRepository.recuperaProdutosCardapio(id);
    } 
}
