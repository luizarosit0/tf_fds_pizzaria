package com.luiza.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.luiza.ex4_lancheriaddd_v1.Aplicacao.Responses.RegistrarUsuarioResponse;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Dados.UsuarioRepository;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Usuario;

@Component
public class RegistrarUsuarioUC {

    private UsuarioRepository usuarioRepository;
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    public RegistrarUsuarioUC (UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder){
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }    

    public RegistrarUsuarioResponse run(String email, String senha) {
        if (usuarioRepository.buscarPorEmail(email) != null) {
            throw new IllegalArgumentException("Email já cadastrado.");
        }
        
        String senhaCodificada = passwordEncoder.encode(senha);
        
        Usuario novoUsuario = new Usuario(email, senhaCodificada, "CLIENTE");
        usuarioRepository.salvar(novoUsuario);
        
        return new RegistrarUsuarioResponse(novoUsuario.getEmail(), novoUsuario.getTipo());
    }
}
