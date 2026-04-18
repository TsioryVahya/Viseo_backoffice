package com.viseo.backoffice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viseo.backoffice.model.PieceDemande;

public interface PieceDemandeRepository extends JpaRepository<PieceDemande, Integer> {

    List<PieceDemande> findAll();

    Optional<PieceDemande> findById(Integer id);

    PieceDemande save(PieceDemande entity);

    void deleteById(Integer id);
}
