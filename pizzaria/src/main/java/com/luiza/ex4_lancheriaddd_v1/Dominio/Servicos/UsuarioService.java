package com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.beans.factory.annotation.Autowired;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Dados.UsuarioRepository;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Usuario;

public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.buscarPorEmail(email);
    }

    public void salvar(Usuario usuario) {
        usuarioRepository.salvar(usuario);
    }
}
