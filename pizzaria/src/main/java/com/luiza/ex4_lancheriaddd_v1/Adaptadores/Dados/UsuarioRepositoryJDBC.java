package com.luiza.ex4_lancheriaddd_v1.Adaptadores.Dados;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Dados.UsuarioRepository;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Usuario;

@Repository
public class UsuarioRepositoryJDBC implements UsuarioRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UsuarioRepositoryJDBC(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Usuario buscarPorEmail(String email) {
        String sql = "SELECT * FROM usuarios WHERE email = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                new Usuario(
                    rs.getString("email"),
                    rs.getString("senha"),
                    rs.getString("tipo")
                ), email
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void salvar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (email, senha, tipo) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, usuario.getEmail(), usuario.getSenha(), usuario.getTipo());
    }
}
