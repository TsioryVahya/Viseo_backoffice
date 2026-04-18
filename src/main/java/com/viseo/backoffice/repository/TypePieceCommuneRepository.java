package com.viseo.backoffice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viseo.backoffice.model.TypePieceCommune;

public interface TypePieceCommuneRepository extends JpaRepository<TypePieceCommune, Integer> {

    List<TypePieceCommune> findAll();

    Optional<TypePieceCommune> findById(Integer id);

    TypePieceCommune save(TypePieceCommune entity);

    void deleteById(Integer id);
}
