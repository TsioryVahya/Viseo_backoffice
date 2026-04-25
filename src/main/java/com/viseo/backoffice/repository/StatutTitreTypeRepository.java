package com.viseo.backoffice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viseo.backoffice.model.StatutTitreType;

public interface StatutTitreTypeRepository extends JpaRepository<StatutTitreType, Integer> {

    Optional<StatutTitreType> findByLibelle(String libelle);
}
