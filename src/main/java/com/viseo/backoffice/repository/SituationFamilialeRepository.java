package com.viseo.backoffice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viseo.backoffice.model.SituationFamiliale;

public interface SituationFamilialeRepository extends JpaRepository<SituationFamiliale, Integer> {

    List<SituationFamiliale> findAll();

    Optional<SituationFamiliale> findById(Integer id);

    SituationFamiliale save(SituationFamiliale entity);

    void deleteById(Integer id);
}
