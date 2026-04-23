package com.viseo.backoffice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viseo.backoffice.model.TypePieceSpecifique;

public interface TypePieceSpecifiqueRepository extends JpaRepository<TypePieceSpecifique, Integer> {

    List<TypePieceSpecifique> findAll();

    Optional<TypePieceSpecifique> findById(Integer id);

    List<TypePieceSpecifique> findByTypeVisa_Id(Integer idTypeVisa);

    List<TypePieceSpecifique> findByTypeVisa_IdAndObligatoire(Integer idTypeVisa, Boolean obligatoire);

    default List<TypePieceSpecifique> findByIdTypeVisa(Integer idTypeVisa) {
        return findByTypeVisa_Id(idTypeVisa);
    }

    default List<TypePieceSpecifique> findByIdTypeVisaAndObligatoire(Integer idTypeVisa, Boolean obligatoire) {
        return findByTypeVisa_IdAndObligatoire(idTypeVisa, obligatoire);
    }

    TypePieceSpecifique save(TypePieceSpecifique entity);

    void deleteById(Integer id);
}
