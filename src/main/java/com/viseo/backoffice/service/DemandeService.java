package com.viseo.backoffice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.viseo.backoffice.model.Demande;
import com.viseo.backoffice.repository.DemandeRepository;

@Service
public class DemandeService {

    private final DemandeRepository demandeRepository;

    public DemandeService(DemandeRepository demandeRepository) {
        this.demandeRepository = demandeRepository;
    }

    public List<Demande> findAll() {
        return demandeRepository.findAll();
    }

    public Optional<Demande> findById(Integer id) {
        return demandeRepository.findById(id);
    }

    public Demande save(Demande entity) {
        return demandeRepository.save(entity);
    }

    public void deleteById(Integer id) {
        demandeRepository.deleteById(id);
    }
}
