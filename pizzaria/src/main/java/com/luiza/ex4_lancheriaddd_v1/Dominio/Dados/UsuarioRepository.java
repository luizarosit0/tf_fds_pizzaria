package com.luiza.ex4_lancheriaddd_v1.Dominio.Dados;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Usuario;

public interface UsuarioRepository {
    Usuario buscarPorEmail(String email);
    void salvar(Usuario usuario);
}
