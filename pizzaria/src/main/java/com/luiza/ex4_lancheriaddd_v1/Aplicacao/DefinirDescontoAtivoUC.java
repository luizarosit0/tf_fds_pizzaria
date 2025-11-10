package com.luiza.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.luiza.ex4_lancheriaddd_v1.Aplicacao.Responses.DefinirDescontoAtivoResponse;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.TipoDesconto;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos.DescontoServiceI;

@Component
public class DefinirDescontoAtivoUC {
    private final DescontoServiceI descontoService; 

    @Autowired
    public DefinirDescontoAtivoUC(DescontoServiceI descontoService){
        this.descontoService = descontoService;
    }

    public DefinirDescontoAtivoResponse run(String tipo) {
        // no caso de String inválida
        TipoDesconto tipoDesconto;
        try {
            tipoDesconto = TipoDesconto.valueOf(tipo.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Tipo de desconto inválido: " + tipo);
        }

        descontoService.definirTipoDescontoAtivo(tipoDesconto);
        
        return new DefinirDescontoAtivoResponse(tipoDesconto.name());
    }
}
