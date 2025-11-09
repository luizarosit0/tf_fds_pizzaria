package com.luiza.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Dados.UsuarioRepository;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Usuario;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Servicos.AutenticacaoService;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AutenticacaoController {

    private final AutenticacaoService autenticacaoService;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public AutenticacaoController(AutenticacaoService autenticacaoService, UsuarioRepository usuarioRepository) {
        this.autenticacaoService = autenticacaoService;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String senha) {
        return autenticacaoService.login(email, senha)
                ? "Login realizado com sucesso!"
                : "Credenciais inválidas.";
    }

    @PostMapping("/logout")
    public String logout() {
        autenticacaoService.logout();
        return "Logout realizado.";
    }

    @PostMapping("/registrar")
    public String registrar(@RequestParam String email, @RequestParam String senha) {
        if (usuarioRepository.buscarPorEmail(email) != null)
            return "Email já cadastrado!";
        usuarioRepository.salvar(new Usuario(email, senha, "CLIENTE"));
        return "Usuário registrado com sucesso!";
    }
}
