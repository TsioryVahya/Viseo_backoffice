package com.viseo.backoffice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viseo.backoffice.model.TypeVisa;

public interface TypeVisaRepository extends JpaRepository<TypeVisa, Integer> {

    List<TypeVisa> findAll();

    Optional<TypeVisa> findById(Integer id);

    TypeVisa save(TypeVisa entity);

    void deleteById(Integer id);
}
