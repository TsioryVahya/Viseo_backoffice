package com.viseo.backoffice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viseo.backoffice.model.VisaTransformable;

public interface VisaTransformableRepository extends JpaRepository<VisaTransformable, Integer> {

    List<VisaTransformable> findAll();

    Optional<VisaTransformable> findById(Integer id);

    Optional<VisaTransformable> findByNumeroReference(String numeroReference);

    VisaTransformable save(VisaTransformable entity);

    void deleteById(Integer id);
}
