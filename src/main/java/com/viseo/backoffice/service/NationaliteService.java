package com.viseo.backoffice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.viseo.backoffice.model.Nationalite;
import com.viseo.backoffice.repository.NationaliteRepository;

@Service
public class NationaliteService {

    private final NationaliteRepository nationaliteRepository;

    public NationaliteService(NationaliteRepository nationaliteRepository) {
        this.nationaliteRepository = nationaliteRepository;
    }

    public List<Nationalite> findAll() {
        return nationaliteRepository.findAll();
    }

    public Optional<Nationalite> findById(Integer id) {
        return nationaliteRepository.findById(id);
    }

    public Nationalite save(Nationalite entity) {
        return nationaliteRepository.save(entity);
    }

    public void deleteById(Integer id) {
        nationaliteRepository.deleteById(id);
    }
}
