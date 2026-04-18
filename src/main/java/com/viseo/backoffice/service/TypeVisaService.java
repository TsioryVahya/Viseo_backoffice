package com.viseo.backoffice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.viseo.backoffice.model.TypeVisa;
import com.viseo.backoffice.repository.TypeVisaRepository;

@Service
public class TypeVisaService {

    private final TypeVisaRepository typeVisaRepository;

    public TypeVisaService(TypeVisaRepository typeVisaRepository) {
        this.typeVisaRepository = typeVisaRepository;
    }

    public List<TypeVisa> findAll() {
        return typeVisaRepository.findAll();
    }

    public Optional<TypeVisa> findById(Integer id) {
        return typeVisaRepository.findById(id);
    }

    public TypeVisa save(TypeVisa entity) {
        return typeVisaRepository.save(entity);
    }

    public void deleteById(Integer id) {
        typeVisaRepository.deleteById(id);
    }
}
