package com.luiza.ex4_lancheriaddd_v1.Adaptadores.Dados;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Dados.IngredientesRepository;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Dados.ReceitasRepository; 
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Ingrediente;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.ItemReceita; // import da nova classe 
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Receita; 

@Repository
public class ReceitasRepositoryJDBC implements ReceitasRepository {
    private JdbcTemplate jdbcTemplate;
    private IngredientesRepository ingredientesRepository; // mantido apenas por causa do construtor, mas não usado na recuperação de receita

    @Autowired
    public ReceitasRepositoryJDBC(JdbcTemplate jdbcTemplate, IngredientesRepository ingredientesRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.ingredientesRepository = ingredientesRepository;
    }

    @Override
    public Receita recuperaReceita(long id) {
        String sqlReceita = "SELECT r.id, r.titulo FROM receitas r WHERE r.id = ?";
        
        List<Receita> receitas = this.jdbcTemplate.query(
            sqlReceita,
            ps -> ps.setLong(1, id),
            (rs, rowNum) -> {
                long receitaId = rs.getLong("id");
                String titulo = rs.getString("titulo");

                List<ItemReceita> composicao = recuperaComposicaoReceita(receitaId); // alt

                return new Receita(receitaId, titulo, composicao); // alt
            }
        );
        return receitas.isEmpty() ? null : receitas.get(0);
    }
    
    // metodo novo que recupera o ingrediente e a quantidade para a receita
    private List<ItemReceita> recuperaComposicaoReceita(long idReceita) {
        
        String sqlComposicao = "SELECT ri.ingrediente_id, ri.quantidade_necessaria, i.descricao " +
                               "FROM receita_ingrediente ri " + 
                               "JOIN ingredientes i ON ri.ingrediente_id = i.id " +
                               "WHERE ri.receita_id = ?";

        return this.jdbcTemplate.query(
            sqlComposicao,
            ps -> ps.setLong(1, idReceita),
            (rs, rowNum) -> {
                long ingredienteId = rs.getLong("ingrediente_id");
                String descricao = rs.getString("descricao");
                int quantidadeNecessaria = rs.getInt("quantidade_necessaria"); // le o campo quantidade_necessaria

                Ingrediente ingrediente = new Ingrediente(ingredienteId, descricao); 
                
                return new ItemReceita(ingrediente, quantidadeNecessaria); // cria o obj ItemReceita
            }
        );
    }
}