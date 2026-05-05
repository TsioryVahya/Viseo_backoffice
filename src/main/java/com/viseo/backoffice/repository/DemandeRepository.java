package com.viseo.backoffice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.viseo.backoffice.model.Demande;

public interface DemandeRepository extends JpaRepository<Demande, Integer> {

    List<Demande> findAll();

    Optional<Demande> findById(Integer id);

    Demande save(Demande entity);

    void deleteById(Integer id);

    List<Demande> findByDemandeurId(Integer demandeurId);

    @Query("""
        SELECT d FROM Demande d
        WHERE (
                        SELECT sd.statutDemandeType.id
            FROM StatutDemande sd
            WHERE sd.demande = d
                            AND sd.dateChangement = (
                                SELECT MAX(sd2.dateChangement)
                                FROM StatutDemande sd2
                                WHERE sd2.demande = d
                            )
        ) IN :statutIds
        ORDER BY (
                        SELECT MAX(sd3.dateChangement)
                        FROM StatutDemande sd3
                        WHERE sd3.demande = d
        ) DESC
    """)
    Page<Demande> findDemandesByDerniersStatuts(
            @Param("statutIds") List<Integer> statutIds,
            Pageable pageable);
}
