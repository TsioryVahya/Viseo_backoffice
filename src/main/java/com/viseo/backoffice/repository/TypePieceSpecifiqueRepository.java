package com.viseo.backoffice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viseo.backoffice.model.TypePieceSpecifique;

public interface TypePieceSpecifiqueRepository extends JpaRepository<TypePieceSpecifique, Integer> {

    List<TypePieceSpecifique> findAll();

    Optional<TypePieceSpecifique> findById(Integer id);

    TypePieceSpecifique save(TypePieceSpecifique entity);

    void deleteById(Integer id);
}
