package com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Dados.UsuarioRepository;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Usuario;

@Service
public class AutenticacaoService {

    private final UsuarioRepository usuarioRepository;
    private Usuario usuarioLogado;

    @Autowired
    public AutenticacaoService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        inicializarMaster();
    }

    private void inicializarMaster() {
        Usuario master = usuarioRepository.buscarPorEmail("master@pizzaria.com");
        if (master == null) {
            usuarioRepository.salvar(new Usuario("master@pizzaria.com", "1411", "MASTER"));
            // System.out.println("Usuário master criado: master@pizzaria.com / senha: 1411");
        }
    }

    public boolean login(String email, String senha) {
        Usuario usuario = usuarioRepository.buscarPorEmail(email);
        if (usuario != null && usuario.getSenha().equals(senha)) {
            this.usuarioLogado = usuario;
            return true;
        }
        return false;
    }

    public void logout() {
        this.usuarioLogado = null;
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public boolean isAutenticado() {
        return usuarioLogado != null;
    }

    public boolean isMaster() {
        return usuarioLogado != null && usuarioLogado.isMaster();
    }
}
