package com.luiza.ex4_lancheriaddd_v1.Adaptadores.Dados;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EstoqueJPARepository extends JpaRepository<ItemEstoqueBD, Long> {

    Optional<ItemEstoqueBD> findByIngredienteId(long ingredienteId);
}