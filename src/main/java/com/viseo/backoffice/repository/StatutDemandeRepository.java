package com.viseo.backoffice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viseo.backoffice.model.StatutDemande;

public interface StatutDemandeRepository extends JpaRepository<StatutDemande, Integer> {

    List<StatutDemande> findAll();

    Optional<StatutDemande> findById(Integer id);

    StatutDemande save(StatutDemande entity);

    void deleteById(Integer id);
}
