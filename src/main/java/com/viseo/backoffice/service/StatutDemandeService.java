package com.viseo.backoffice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.viseo.backoffice.model.StatutDemande;
import com.viseo.backoffice.repository.StatutDemandeRepository;

@Service
public class StatutDemandeService {

    private final StatutDemandeRepository statutDemandeRepository;

    public StatutDemandeService(StatutDemandeRepository statutDemandeRepository) {
        this.statutDemandeRepository = statutDemandeRepository;
    }

    public List<StatutDemande> findAll() {
        return statutDemandeRepository.findAll();
    }

    public Optional<StatutDemande> findById(Integer id) {
        return statutDemandeRepository.findById(id);
    }

    public StatutDemande save(StatutDemande entity) {
        return statutDemandeRepository.save(entity);
    }

    public void deleteById(Integer id) {
        statutDemandeRepository.deleteById(id);
    }
}
