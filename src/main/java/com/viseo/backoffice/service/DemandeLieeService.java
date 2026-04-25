package com.viseo.backoffice.service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.viseo.backoffice.model.Demande;
import com.viseo.backoffice.model.DemandeLiee;
import com.viseo.backoffice.repository.DemandeLieeRepository;

@Service
public class DemandeLieeService {

    private final DemandeLieeRepository demandeLieeRepository;

    public DemandeLieeService(DemandeLieeRepository demandeLieeRepository) {
        this.demandeLieeRepository = demandeLieeRepository;
    }

    public DemandeLiee lier(Demande demandeOrigine, Demande demandeLiee, String typeLien) {
        String typeLienNormalise = typeLien == null ? "" : typeLien.trim().toLowerCase(Locale.ROOT);
        if (!"duplicata".equals(typeLienNormalise) && !"transfert".equals(typeLienNormalise)) {
            throw new IllegalArgumentException("type_lien doit etre 'duplicata' ou 'transfert'.");
        }

        DemandeLiee lien = new DemandeLiee();
        lien.setDemandeOrigine(demandeOrigine);
        lien.setDemandeLiee(demandeLiee);
        lien.setTypeLien(typeLienNormalise);
        return demandeLieeRepository.save(lien);
    }

    public List<DemandeLiee> findByOrigine(Demande demandeOrigine) {
        return demandeLieeRepository.findByDemandeOrigine(demandeOrigine);
    }

    public Optional<DemandeLiee> findByDemandeLiee(Demande demande) {
        return demandeLieeRepository.findByDemandeLiee(demande);
    }
}
