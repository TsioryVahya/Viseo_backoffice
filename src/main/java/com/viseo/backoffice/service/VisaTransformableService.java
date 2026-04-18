package com.viseo.backoffice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.viseo.backoffice.model.VisaTransformable;
import com.viseo.backoffice.repository.VisaTransformableRepository;

@Service
public class VisaTransformableService {

    private final VisaTransformableRepository visaTransformableRepository;

    public VisaTransformableService(VisaTransformableRepository visaTransformableRepository) {
        this.visaTransformableRepository = visaTransformableRepository;
    }

    public List<VisaTransformable> findAll() {
        return visaTransformableRepository.findAll();
    }

    public Optional<VisaTransformable> findById(Integer id) {
        return visaTransformableRepository.findById(id);
    }

    public VisaTransformable save(VisaTransformable entity) {
        return visaTransformableRepository.save(entity);
    }

    public void deleteById(Integer id) {
        visaTransformableRepository.deleteById(id);
    }
}
