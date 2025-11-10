package com.luiza.ex4_lancheriaddd_v1.Adaptadores.Seguranca;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Dados.UsuarioRepository;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // O "username" no nosso sistema é o email
        Usuario usuario = usuarioRepository.buscarPorEmail(email);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuário não encontrado com o email: " + email);
        }

        // Converte o "tipo" (ex: "MASTER") em uma "Autoridade" que o Spring entende
        // O Spring Security exige o prefixo "ROLE_"
        Collection<GrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority("ROLE_" + usuario.getTipo().toUpperCase())
        );

        // Cria e retorna um objeto User do Spring Security
        return new User(usuario.getEmail(), usuario.getSenha(), authorities);
    }
}