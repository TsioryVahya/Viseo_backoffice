package com.viseo.backoffice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viseo.backoffice.model.StatutVisa;
import com.viseo.backoffice.model.Visa;

public interface StatutVisaRepository extends JpaRepository<StatutVisa, Integer> {

    List<StatutVisa> findByVisa(Visa visa);

    Optional<StatutVisa> findFirstByVisaOrderByDateChangementDesc(Visa visa);
}
