package com.viseo.backoffice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.viseo.backoffice.model.SituationFamiliale;
import com.viseo.backoffice.repository.SituationFamilialeRepository;

@Service
public class SituationFamilialeService {

    private final SituationFamilialeRepository situationFamilialeRepository;

    public SituationFamilialeService(SituationFamilialeRepository situationFamilialeRepository) {
        this.situationFamilialeRepository = situationFamilialeRepository;
    }

    public List<SituationFamiliale> findAll() {
        return situationFamilialeRepository.findAll();
    }

    public Optional<SituationFamiliale> findById(Integer id) {
        return situationFamilialeRepository.findById(id);
    }

    public SituationFamiliale save(SituationFamiliale entity) {
        return situationFamilialeRepository.save(entity);
    }

    public void deleteById(Integer id) {
        situationFamilialeRepository.deleteById(id);
    }
}
