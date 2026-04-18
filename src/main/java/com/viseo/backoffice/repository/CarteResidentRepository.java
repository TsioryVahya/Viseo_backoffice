package com.viseo.backoffice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viseo.backoffice.model.CarteResident;

public interface CarteResidentRepository extends JpaRepository<CarteResident, Integer> {

    List<CarteResident> findAll();

    Optional<CarteResident> findById(Integer id);

    CarteResident save(CarteResident entity);

    void deleteById(Integer id);
}
