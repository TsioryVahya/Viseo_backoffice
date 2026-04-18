package com.viseo.backoffice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viseo.backoffice.model.Demandeur;

public interface DemandeurRepository extends JpaRepository<Demandeur, Integer> {

    List<Demandeur> findAll();

    Optional<Demandeur> findById(Integer id);

    Demandeur save(Demandeur entity);

    void deleteById(Integer id);
}
