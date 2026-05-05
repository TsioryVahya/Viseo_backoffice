package com.viseo.backoffice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viseo.backoffice.model.Demande;
import com.viseo.backoffice.model.DemandeLiee;

public interface DemandeLieeRepository extends JpaRepository<DemandeLiee, Integer> {

    Optional<DemandeLiee> findByDemandeLiee(Demande demande);

    List<DemandeLiee> findByDemandeOrigine(Demande demande);

    Optional<DemandeLiee> findByDemandeOrigineAndTypeLien(Demande demande, String typeLien);
}
