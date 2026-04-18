package com.viseo.backoffice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.viseo.backoffice.model.Demandeur;
import com.viseo.backoffice.repository.DemandeurRepository;

@Service
public class DemandeurService {

    private final DemandeurRepository demandeurRepository;

    public DemandeurService(DemandeurRepository demandeurRepository) {
        this.demandeurRepository = demandeurRepository;
    }

    public List<Demandeur> findAll() {
        return demandeurRepository.findAll();
    }

    public Optional<Demandeur> findById(Integer id) {
        return demandeurRepository.findById(id);
    }

    public Demandeur save(Demandeur entity) {
        return demandeurRepository.save(entity);
    }

    public void deleteById(Integer id) {
        demandeurRepository.deleteById(id);
    }
}
