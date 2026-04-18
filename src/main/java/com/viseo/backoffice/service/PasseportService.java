package com.viseo.backoffice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.viseo.backoffice.model.Passeport;
import com.viseo.backoffice.repository.PasseportRepository;

@Service
public class PasseportService {

    private final PasseportRepository passeportRepository;

    public PasseportService(PasseportRepository passeportRepository) {
        this.passeportRepository = passeportRepository;
    }

    public List<Passeport> findAll() {
        return passeportRepository.findAll();
    }

    public Optional<Passeport> findById(Integer id) {
        return passeportRepository.findById(id);
    }

    public Passeport save(Passeport entity) {
        return passeportRepository.save(entity);
    }

    public void deleteById(Integer id) {
        passeportRepository.deleteById(id);
    }
}
