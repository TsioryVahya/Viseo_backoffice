package com.viseo.backoffice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viseo.backoffice.model.CarteResident;
import com.viseo.backoffice.model.StatutCarteResident;

public interface StatutCarteResidentRepository extends JpaRepository<StatutCarteResident, Integer> {

    List<StatutCarteResident> findByCarteResident(CarteResident carte);

    Optional<StatutCarteResident> findFirstByCarteResidentOrderByDateChangementDesc(CarteResident carte);
}
