package com.viseo.backoffice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viseo.backoffice.model.Visa;

public interface VisaRepository extends JpaRepository<Visa, Integer> {

    List<Visa> findAll();

    Optional<Visa> findById(Integer id);

    Visa save(Visa entity);

    void deleteById(Integer id);
}
