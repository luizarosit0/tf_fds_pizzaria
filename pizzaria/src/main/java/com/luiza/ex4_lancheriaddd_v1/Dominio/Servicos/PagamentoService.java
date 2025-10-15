package com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos;

//Responsável pelos meios de pagamento

import org.springframework.stereotype.Service;

//O serviço de pagamentos pode ser um “fake” que responde sempre que o 
//pagamento foi efetuado. 
@Service
public class PagamentoService implements PagamentoServiceI {

    @Override
    public boolean pagar(double valor) {
        System.out.println("Processando pagamento de R$" + valor + "... Pagamento Aprovado!");
        return true;
    }
}