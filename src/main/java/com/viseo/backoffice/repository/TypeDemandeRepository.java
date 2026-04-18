package com.viseo.backoffice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viseo.backoffice.model.TypeDemande;

public interface TypeDemandeRepository extends JpaRepository<TypeDemande, Integer> {

    List<TypeDemande> findAll();

    Optional<TypeDemande> findById(Integer id);

    TypeDemande save(TypeDemande entity);

    void deleteById(Integer id);
}
