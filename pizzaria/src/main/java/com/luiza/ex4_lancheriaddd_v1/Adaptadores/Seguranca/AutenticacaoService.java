package com.luiza.ex4_lancheriaddd_v1.Adaptadores.Seguranca;

import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Dados.UsuarioRepository;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Usuario;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos.UsuarioService;

@Service
public class AutenticacaoService implements UserDetailsService {
    private UsuarioService usuarioService;
    
    @Autowired
    public AutenticacaoService(UsuarioService usuarioService){
        this.usuarioService = usuarioService; 
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //"username" = email
        Usuario usuario = usuarioService.buscarPorEmail(email);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuário não encontrado com o email: " + email);
        }

        // converte "tipo" ("MASTER") --> "autoridade"
        Collection<GrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority("ROLE_" + usuario.getTipo().toUpperCase())
        );

        return new User(usuario.getEmail(), usuario.getSenha(), authorities);
    }
}