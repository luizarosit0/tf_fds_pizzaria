package com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades;

public class Usuario {
    private String email;
    private String senha;
    private String tipo;

    public Usuario(String email, String senha, String tipo) {
        this.email = email;
        this.senha = senha;
        this.tipo = tipo;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public String getTipo() {
        return tipo;
    }

    public boolean isMaster() {
        return "MASTER".equalsIgnoreCase(tipo);
    }
}
