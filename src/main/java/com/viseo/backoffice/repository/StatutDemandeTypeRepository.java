package com.viseo.backoffice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.viseo.backoffice.model.StatutDemandeType;

public interface StatutDemandeTypeRepository extends JpaRepository<StatutDemandeType, Integer> {

    List<StatutDemandeType> findAll();

    Optional<StatutDemandeType> findById(Integer id);

    StatutDemandeType save(StatutDemandeType entity);

    void deleteById(Integer id);

    /**
     * Recherche un statut par son libellé exact
     * 
     * @param libelle le libellé du statut
     * @return le statut trouvé
     */
    @Query("SELECT s FROM StatutDemandeType s WHERE TRIM(s.libelle) = TRIM(:libelle)")
    Optional<StatutDemandeType> findByLibelle(@Param("libelle") String libelle);
}
