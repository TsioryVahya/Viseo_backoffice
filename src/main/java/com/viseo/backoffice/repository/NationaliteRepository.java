package com.viseo.backoffice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viseo.backoffice.model.Nationalite;

public interface NationaliteRepository extends JpaRepository<Nationalite, Integer> {

    List<Nationalite> findAll();

    Optional<Nationalite> findById(Integer id);

    Nationalite save(Nationalite entity);

    void deleteById(Integer id);
}
