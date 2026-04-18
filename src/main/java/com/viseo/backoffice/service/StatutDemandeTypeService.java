package com.viseo.backoffice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.viseo.backoffice.model.StatutDemandeType;
import com.viseo.backoffice.repository.StatutDemandeTypeRepository;

@Service
public class StatutDemandeTypeService {

    private final StatutDemandeTypeRepository statutDemandeTypeRepository;

    public StatutDemandeTypeService(StatutDemandeTypeRepository statutDemandeTypeRepository) {
        this.statutDemandeTypeRepository = statutDemandeTypeRepository;
    }

    public List<StatutDemandeType> findAll() {
        return statutDemandeTypeRepository.findAll();
    }

    public Optional<StatutDemandeType> findById(Integer id) {
        return statutDemandeTypeRepository.findById(id);
    }

    public StatutDemandeType save(StatutDemandeType entity) {
        return statutDemandeTypeRepository.save(entity);
    }

    public void deleteById(Integer id) {
        statutDemandeTypeRepository.deleteById(id);
    }
}
