package com.viseo.backoffice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viseo.backoffice.model.TypeDemande;

public interface TypeDemandeRepository extends JpaRepository<TypeDemande, Integer> {

    List<TypeDemande> findAll();

    Optional<TypeDemande> findById(Integer id);

    TypeDemande save(TypeDemande entity);

    void deleteById(Integer id);

    /**
     * Recherche un type de demande par son libellé exact
     * 
     * @param libelle le libellé du type de demande
     * @return le type de demande trouvé
     */
    Optional<TypeDemande> findByLibelle(String libelle);

    /**
     * Recherche les types de demande par libellé contenant (insensible à la casse)
     * 
     * @param libelle le libellé à rechercher
     * @return la liste des types correspondants
     */
    List<TypeDemande> findByLibelleIgnoreCaseContaining(String libelle);
}
