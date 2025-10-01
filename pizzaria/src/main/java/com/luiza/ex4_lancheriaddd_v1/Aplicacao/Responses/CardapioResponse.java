package com.luiza.ex4_lancheriaddd_v1.Aplicacao.Responses;

import java.util.List;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Cardapio;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;

public class CardapioResponse {
    private Cardapio cardapio;
    private List<Produto> sugestoesDoChef;
    
    public CardapioResponse(Cardapio cardapio, List<Produto> sugestoesDoChef) {
        this.cardapio = cardapio;
        this.sugestoesDoChef = sugestoesDoChef;
    }

    public Cardapio getCardapio() {
        return cardapio;
    }

    public List<Produto> getSugestoesDoChef() {
        return sugestoesDoChef;
    }
}
