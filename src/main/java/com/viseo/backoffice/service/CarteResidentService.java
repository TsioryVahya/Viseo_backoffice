package com.viseo.backoffice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.viseo.backoffice.model.CarteResident;
import com.viseo.backoffice.model.Demande;
import com.viseo.backoffice.model.StatutCarteResident;
import com.viseo.backoffice.model.StatutTitreType;
import com.viseo.backoffice.repository.CarteResidentRepository;
import com.viseo.backoffice.repository.StatutCarteResidentRepository;
import com.viseo.backoffice.repository.StatutTitreTypeRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CarteResidentService {

    private final CarteResidentRepository carteResidentRepository;
    private final StatutCarteResidentRepository statutCarteResidentRepository;
    private final StatutTitreTypeRepository statutTitreTypeRepository;

    public CarteResidentService(
            CarteResidentRepository carteResidentRepository,
            StatutCarteResidentRepository statutCarteResidentRepository,
            StatutTitreTypeRepository statutTitreTypeRepository) {
        this.carteResidentRepository = carteResidentRepository;
        this.statutCarteResidentRepository = statutCarteResidentRepository;
        this.statutTitreTypeRepository = statutTitreTypeRepository;
    }

    public List<CarteResident> findAll() {
        return carteResidentRepository.findAll();
    }

    public Optional<CarteResident> findByReference(String reference) {
        return carteResidentRepository.findByReference(reference);
    }

    public CarteResident findById(Integer id) {
        return carteResidentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Carte de resident introuvable: " + id));
    }

    public Optional<CarteResident> findOptionalById(Integer id) {
        return carteResidentRepository.findById(id);
    }

    public void changerStatut(CarteResident carte, Integer idStatutType, String commentaire) {
        StatutTitreType statutType = statutTitreTypeRepository.findById(idStatutType)
                .orElseThrow(() -> new EntityNotFoundException("Statut titre type introuvable: " + idStatutType));

        StatutCarteResident statutCarte = new StatutCarteResident();
        statutCarte.setCarteResident(carte);
        statutCarte.setStatutType(statutType);
        statutCarte.setDateChangement(LocalDateTime.now());
        statutCarte.setCommentaire(commentaire);
        statutCarteResidentRepository.save(statutCarte);
    }

    public Optional<StatutCarteResident> findStatutActuel(CarteResident carte) {
        return statutCarteResidentRepository.findFirstByCarteResidentOrderByDateChangementDesc(carte);
    }

    public CarteResident dupliquerAvecNouvelledemande(CarteResident carteSource, Demande nouvelleDemande) {
        CarteResident nouvelleCarte = new CarteResident();
        nouvelleCarte.setDemande(nouvelleDemande);
        nouvelleCarte.setReference(carteSource.getReference());
        nouvelleCarte.setDateDebut(carteSource.getDateDebut());
        nouvelleCarte.setDateFin(carteSource.getDateFin());
        nouvelleCarte.setPasseport(carteSource.getPasseport());
        return carteResidentRepository.save(nouvelleCarte);
    }

    public CarteResident save(CarteResident entity) {
        return carteResidentRepository.save(entity);
    }

    public void deleteById(Integer id) {
        carteResidentRepository.deleteById(id);
    }
}
