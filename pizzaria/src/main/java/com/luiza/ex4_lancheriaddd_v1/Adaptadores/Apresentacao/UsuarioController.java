package com.luiza.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luiza.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.Presenters.RegistrarUsuarioPresenter;
import com.luiza.ex4_lancheriaddd_v1.Aplicacao.RegistrarUsuarioUC;
import com.luiza.ex4_lancheriaddd_v1.Aplicacao.Responses.RegistrarUsuarioResponse;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin("*")
public class UsuarioController {
    private RegistrarUsuarioUC registrarUsuarioUC;

    @Autowired
    public UsuarioController (RegistrarUsuarioUC registrarUsuarioUC){
        this.registrarUsuarioUC = registrarUsuarioUC;
    }

    public record RegistroInput(String email, String senha) {}

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody RegistroInput input) {
        try {
            RegistrarUsuarioResponse response = registrarUsuarioUC.run(input.email(), input.senha());

            // Response --> Presenter
            RegistrarUsuarioPresenter presenter = new RegistrarUsuarioPresenter(
                response.email(),
                response.tipo()
            );
            
            return ResponseEntity.status(HttpStatus.CREATED).body(presenter);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
