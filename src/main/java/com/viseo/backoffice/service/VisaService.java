package com.viseo.backoffice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.viseo.backoffice.model.Visa;
import com.viseo.backoffice.repository.VisaRepository;

@Service
public class VisaService {

    private final VisaRepository visaRepository;

    public VisaService(VisaRepository visaRepository) {
        this.visaRepository = visaRepository;
    }

    public List<Visa> findAll() {
        return visaRepository.findAll();
    }

    public Optional<Visa> findById(Integer id) {
        return visaRepository.findById(id);
    }

    public Visa save(Visa entity) {
        return visaRepository.save(entity);
    }

    public void deleteById(Integer id) {
        visaRepository.deleteById(id);
    }
}
