package com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Dados.CardapioRepository;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.CabecalhoCardapio;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Cardapio;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Produto; 

@Service
public class CardapioService {
    private CardapioRepository cardapioRepository;

    @Autowired
    public CardapioService(CardapioRepository cardapioRepository){
        this.cardapioRepository = cardapioRepository;
    }

    public Cardapio recuperaCardapio(long Id){ // cardapio completo
        return cardapioRepository.recuperaPorId(Id);
    }

    public List<CabecalhoCardapio> recuperaListaDeCardapios(){ // so o cabecalho do cardapio 
        return cardapioRepository.cardapiosDisponiveis();
    }

    public List<Produto> recuperaSugestoesDoChef(){ // produtos destacados como sugestoes 
        return cardapioRepository.indicacoesDoChef();
    }
}
