package com.viseo.backoffice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.viseo.backoffice.model.StatutTitreType;
import com.viseo.backoffice.model.StatutVisa;
import com.viseo.backoffice.model.Visa;
import com.viseo.backoffice.repository.StatutTitreTypeRepository;
import com.viseo.backoffice.repository.StatutVisaRepository;
import com.viseo.backoffice.repository.VisaRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class VisaService {

    private final VisaRepository visaRepository;
    private final StatutVisaRepository statutVisaRepository;
    private final StatutTitreTypeRepository statutTitreTypeRepository;

    public VisaService(
            VisaRepository visaRepository,
            StatutVisaRepository statutVisaRepository,
            StatutTitreTypeRepository statutTitreTypeRepository) {
        this.visaRepository = visaRepository;
        this.statutVisaRepository = statutVisaRepository;
        this.statutTitreTypeRepository = statutTitreTypeRepository;
    }

    public List<Visa> findAll() {
        return visaRepository.findAll();
    }

    public Optional<Visa> findByReference(String reference) {
        return visaRepository.findByReference(reference);
    }

    public Visa findById(Integer id) {
        return visaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Visa introuvable: " + id));
    }

    public Optional<Visa> findOptionalById(Integer id) {
        return visaRepository.findById(id);
    }

    public void changerStatut(Visa visa, Integer idStatutType, String commentaire) {
        StatutTitreType statutType = statutTitreTypeRepository.findById(idStatutType)
                .orElseThrow(() -> new EntityNotFoundException("Statut titre type introuvable: " + idStatutType));

        StatutVisa statutVisa = new StatutVisa();
        statutVisa.setVisa(visa);
        statutVisa.setStatutType(statutType);
        statutVisa.setDateChangement(LocalDateTime.now());
        statutVisa.setCommentaire(commentaire);
        statutVisaRepository.save(statutVisa);
    }

    public Optional<StatutVisa> findStatutActuel(Visa visa) {
        return statutVisaRepository.findFirstByVisaOrderByDateChangementDesc(visa);
    }

    public Visa save(Visa entity) {
        return visaRepository.save(entity);
    }

    public void deleteById(Integer id) {
        visaRepository.deleteById(id);
    }
}
