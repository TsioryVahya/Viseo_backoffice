package com.viseo.backoffice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.viseo.backoffice.model.TypeDemande;
import com.viseo.backoffice.repository.TypeDemandeRepository;

@Service
public class TypeDemandeService {

    private final TypeDemandeRepository typeDemandeRepository;

    public TypeDemandeService(TypeDemandeRepository typeDemandeRepository) {
        this.typeDemandeRepository = typeDemandeRepository;
    }

    public List<TypeDemande> findAll() {
        return typeDemandeRepository.findAll();
    }

    public Optional<TypeDemande> findById(Integer id) {
        return typeDemandeRepository.findById(id);
    }

    public TypeDemande save(TypeDemande entity) {
        return typeDemandeRepository.save(entity);
    }

    public void deleteById(Integer id) {
        typeDemandeRepository.deleteById(id);
    }
}
