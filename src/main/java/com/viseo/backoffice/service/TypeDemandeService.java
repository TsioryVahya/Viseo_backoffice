package com.viseo.backoffice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.viseo.backoffice.model.TypeDemande;
import com.viseo.backoffice.repository.TypeDemandeRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class TypeDemandeService {

    private final TypeDemandeRepository typeDemandeRepository;

    public TypeDemandeService(TypeDemandeRepository typeDemandeRepository) {
        this.typeDemandeRepository = typeDemandeRepository;
    }

    public List<TypeDemande> findAll() {
        return typeDemandeRepository.findAll();
    }

    /**
     * Retourne tous les types de demande sauf "Nouveau titre".
     */
    public List<TypeDemande> findAllSaufNouveauTitre() {
        return typeDemandeRepository.findByLibelleNot("Nouveau titre");
    }

    /**
     * Retourne un type de demande par id.
     */
    public TypeDemande findById(Integer id) {
        return typeDemandeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Type de demande introuvable: " + id));
    }

    public Optional<TypeDemande> findOptionalById(Integer id) {
        return typeDemandeRepository.findById(id);
    }

    public TypeDemande save(TypeDemande entity) {
        return typeDemandeRepository.save(entity);
    }

    public void deleteById(Integer id) {
        typeDemandeRepository.deleteById(id);
    }
}
