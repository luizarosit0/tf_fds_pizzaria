package com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Usuario;

public interface UsuarioServiceI {
    Usuario buscarPorEmail(String email);
    void salvar(Usuario usuario);
}
