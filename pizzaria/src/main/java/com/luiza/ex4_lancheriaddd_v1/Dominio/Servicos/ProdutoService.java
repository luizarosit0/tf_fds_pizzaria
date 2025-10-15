package com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Dados.CardapioRepository;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;

@Service
public class ProdutoService {
    private ProdutoRepository produtoRepository;

    @Autowired
    public ProdutoService(ProdutoRepository produtoRepository){
        this.produtoRepository = produtoRepository;
    }

    public Produto recuperaProdutoPorid(long id);{
        return produtoRepository.recuperaProdutoPorid(id);
    }

    public List<Produto> recuperaProdutosCardapio(long id);{
        return produtoRepository.recuperaProdutosCardapio(id);
    }
}
