package com.viseo.backoffice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viseo.backoffice.model.StatutDemandeType;

public interface StatutDemandeTypeRepository extends JpaRepository<StatutDemandeType, Integer> {

    List<StatutDemandeType> findAll();

    Optional<StatutDemandeType> findById(Integer id);

    StatutDemandeType save(StatutDemandeType entity);

    void deleteById(Integer id);
}
