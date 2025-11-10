package com.luiza.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.luiza.ex4_lancheriaddd_v1.Aplicacao.Responses.RegistrarUsuarioResponse;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Usuario;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos.UsuarioServiceI;

@Component
public class RegistrarUsuarioUC {

    private UsuarioServiceI usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    public RegistrarUsuarioUC (UsuarioServiceI usuarioService){
        this.usuarioService = usuarioService;
    }    

    public RegistrarUsuarioResponse run(String email, String senha) {
        if (usuarioService.buscarPorEmail(email) != null) {
            throw new IllegalArgumentException("Email já cadastrado.");
        }
        
        String senhaCodificada = passwordEncoder.encode(senha);
        
        Usuario novoUsuario = new Usuario(email, senhaCodificada, "CLIENTE");
        usuarioService.salvar(novoUsuario);
        
        return new RegistrarUsuarioResponse(novoUsuario.getEmail(), novoUsuario.getTipo());
    }
}
