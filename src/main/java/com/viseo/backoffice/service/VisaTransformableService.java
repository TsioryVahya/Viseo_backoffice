package com.viseo.backoffice.service;

import java.util.List;
import java.util.Optional;

import com.viseo.backoffice.model.VisaTransformable;

public interface VisaTransformableService {

    List<VisaTransformable> findAll();

    Optional<VisaTransformable> findById(Integer id);

    Optional<VisaTransformable> findByNumeroReference(String numeroReference);

    VisaTransformable save(VisaTransformable visaTransformable);

    void deleteById(Integer id);
}
