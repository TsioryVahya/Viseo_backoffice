package com.viseo.backoffice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viseo.backoffice.model.Demande;

public interface DemandeRepository extends JpaRepository<Demande, Integer> {

    List<Demande> findAll();

    Optional<Demande> findById(Integer id);

    Demande save(Demande entity);

    void deleteById(Integer id);
}
