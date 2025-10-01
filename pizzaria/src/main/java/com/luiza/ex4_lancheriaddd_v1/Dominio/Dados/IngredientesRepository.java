package com.luiza.ex4_lancheriaddd_v1.Dominio.Dados;

import java.util.List;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Ingrediente;

public interface IngredientesRepository {
    List<Ingrediente> recuperaTodos();
    List<Ingrediente> recuperaIngredientesReceita(long id);
}
