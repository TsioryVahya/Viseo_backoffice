package com.viseo.backoffice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viseo.backoffice.model.Passeport;

public interface PasseportRepository extends JpaRepository<Passeport, Integer> {

    List<Passeport> findAll();

    Optional<Passeport> findById(Integer id);

    Passeport save(Passeport entity);

    void deleteById(Integer id);
}
