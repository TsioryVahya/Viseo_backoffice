package com.viseo.backoffice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viseo.backoffice.model.Demande;
import com.viseo.backoffice.model.PieceDemandeSpecifique;

public interface PieceDemandeSpecifiqueRepository extends JpaRepository<PieceDemandeSpecifique, Integer> {

    List<PieceDemandeSpecifique> findAll();

    Optional<PieceDemandeSpecifique> findById(Integer id);

    PieceDemandeSpecifique save(PieceDemandeSpecifique entity);

    void deleteById(Integer id);

    List<PieceDemandeSpecifique> findByDemande(Demande demande);
}
