package com.luiza.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luiza.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.Presenters.DefinirDescontoAtivoPresenter;
import com.luiza.ex4_lancheriaddd_v1.Aplicacao.DefinirDescontoAtivoUC;
import com.luiza.ex4_lancheriaddd_v1.Aplicacao.Responses.DefinirDescontoAtivoResponse;

@RestController
@RequestMapping("/admin") 
@CrossOrigin("*")
public class AdminController {

    private final DefinirDescontoAtivoUC definirDescontoAtivoUC;

    @Autowired
    public AdminController(DefinirDescontoAtivoUC definirDescontoAtivoUC) {
        this.definirDescontoAtivoUC = definirDescontoAtivoUC;
    }

    public record DescontoInput(String tipo) {}

    @PutMapping("/descontos/definir-ativo")
    public ResponseEntity<?> definirDesconto(@RequestBody DescontoInput input) {
        try {
            DefinirDescontoAtivoResponse response = definirDescontoAtivoUC.run(input.tipo());

            DefinirDescontoAtivoPresenter presenter = new DefinirDescontoAtivoPresenter(
                response.novoTipoAtivo(),
                "Desconto ativo alterado com sucesso."
            );
            
            return ResponseEntity.ok(presenter);

        } catch (IllegalArgumentException e) {
            // se o 'tipo' for inválido
            return ResponseEntity.badRequest().body("Tipo de desconto inválido: " + input.tipo());
        }
    }
}