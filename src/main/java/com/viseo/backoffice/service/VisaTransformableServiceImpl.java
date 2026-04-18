package com.viseo.backoffice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.viseo.backoffice.model.VisaTransformable;
import com.viseo.backoffice.repository.VisaTransformableRepository;

@Service
public class VisaTransformableServiceImpl implements VisaTransformableService {

    private final VisaTransformableRepository visaTransformableRepository;

    public VisaTransformableServiceImpl(VisaTransformableRepository visaTransformableRepository) {
        this.visaTransformableRepository = visaTransformableRepository;
    }

    @Override
    public List<VisaTransformable> findAll() {
        return visaTransformableRepository.findAll();
    }

    @Override
    public Optional<VisaTransformable> findById(Integer id) {
        return visaTransformableRepository.findById(id);
    }

    @Override
    public Optional<VisaTransformable> findByNumeroReference(String numeroReference) {
        return visaTransformableRepository.findByNumeroReference(numeroReference);
    }

    @Override
    public VisaTransformable save(VisaTransformable visaTransformable) {
        return visaTransformableRepository.save(visaTransformable);
    }

    @Override
    public void deleteById(Integer id) {
        visaTransformableRepository.deleteById(id);
    }
}
