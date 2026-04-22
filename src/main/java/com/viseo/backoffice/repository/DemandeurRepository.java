package com.viseo.backoffice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.viseo.backoffice.model.Demandeur;

public interface DemandeurRepository extends JpaRepository<Demandeur, Integer> {

    List<Demandeur> findAll();

    Optional<Demandeur> findById(Integer id);

    Demandeur save(Demandeur entity);

    void deleteById(Integer id);

    Optional<Demandeur> findByNomIgnoreCaseAndPrenomIgnoreCase(String nom, String prenom);

    /**
     * Recherche les demandeurs par nom et prénom avec recherche partielle (insensible à la casse)
     * 
     * @param nom le nom du demandeur (contient)
     * @param prenom le prénom du demandeur (contient)
     * @return la liste des demandeurs correspondants
     */
    @Query("SELECT d FROM Demandeur d WHERE LOWER(d.nom) LIKE LOWER(CONCAT('%', :nom, '%')) AND LOWER(d.prenom) LIKE LOWER(CONCAT('%', :prenom, '%'))")
    List<Demandeur> searchByNomAndPrenom(@Param("nom") String nom, @Param("prenom") String prenom);
}
