package com.luiza.ex4_lancheriaddd_v1.Adaptadores.Dados;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.luiza.ex4_lancheriaddd_v1.Dominio.Dados.EstoqueRepository;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Ingrediente;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.ItemEstoque;

@Repository 
public class EstoqueRepositoryJPA implements EstoqueRepository {

    private final EstoqueJPARepository jpaRepo;

    @Autowired
    public EstoqueRepositoryJPA(EstoqueJPARepository jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public ItemEstoque findByIngrediente(Ingrediente ingrediente) {
        return jpaRepo.findByIngredienteId(ingrediente.getId())
                      .map(ItemEstoqueBD::fromItemEstoqueBD) // Usa o método conversor
                      .orElse(null); 
    }

    @Override
    public void save(ItemEstoque itemEstoque) {
        ItemEstoqueBD itemBD = ItemEstoqueBD.toItemEstoqueBD(itemEstoque); // Converte Domínio -> BD
        jpaRepo.save(itemBD);
    }
}
