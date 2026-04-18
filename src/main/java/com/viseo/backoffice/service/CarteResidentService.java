package com.viseo.backoffice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.viseo.backoffice.model.CarteResident;
import com.viseo.backoffice.repository.CarteResidentRepository;

@Service
public class CarteResidentService {

    private final CarteResidentRepository carteResidentRepository;

    public CarteResidentService(CarteResidentRepository carteResidentRepository) {
        this.carteResidentRepository = carteResidentRepository;
    }

    public List<CarteResident> findAll() {
        return carteResidentRepository.findAll();
    }

    public Optional<CarteResident> findById(Integer id) {
        return carteResidentRepository.findById(id);
    }

    public CarteResident save(CarteResident entity) {
        return carteResidentRepository.save(entity);
    }

    public void deleteById(Integer id) {
        carteResidentRepository.deleteById(id);
    }
}
