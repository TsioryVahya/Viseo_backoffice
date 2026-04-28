package com.viseo.backoffice.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.viseo.backoffice.model.Demande;
import com.viseo.backoffice.model.DemandeLiee;
import com.viseo.backoffice.model.Passeport;
import com.viseo.backoffice.model.StatutDemande;
import com.viseo.backoffice.model.StatutDemandeType;
import com.viseo.backoffice.model.StatutTitreType;
import com.viseo.backoffice.model.StatutVisa;
import com.viseo.backoffice.model.Visa;
import com.viseo.backoffice.repository.DemandeLieeRepository;
import com.viseo.backoffice.repository.DemandeRepository;
import com.viseo.backoffice.repository.PasseportRepository;
import com.viseo.backoffice.repository.StatutDemandeRepository;
import com.viseo.backoffice.repository.StatutDemandeTypeRepository;
import com.viseo.backoffice.repository.StatutTitreTypeRepository;
import com.viseo.backoffice.repository.StatutVisaRepository;
import com.viseo.backoffice.repository.TypeDemandeRepository;
import com.viseo.backoffice.repository.VisaRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class TransfertService {

    private static final Logger log =
        LoggerFactory.getLogger(TransfertService.class);

    private final DemandeRepository demandeRepository;
    private final PasseportRepository passeportRepository;
    private final VisaRepository visaRepository;
    private final StatutVisaRepository statutVisaRepository;
    private final StatutTitreTypeRepository statutTitreTypeRepository;
    private final StatutDemandeRepository statutDemandeRepository;
    private final StatutDemandeTypeRepository statutDemandeTypeRepository;
    private final DemandeLieeRepository demandeLieeRepository;
    private final TypeDemandeRepository typeDemandeRepository;

    public TransfertService(
            DemandeRepository demandeRepository,
            PasseportRepository passeportRepository,
            VisaRepository visaRepository,
            StatutVisaRepository statutVisaRepository,
            StatutTitreTypeRepository statutTitreTypeRepository,
            StatutDemandeRepository statutDemandeRepository,
            StatutDemandeTypeRepository statutDemandeTypeRepository,
            DemandeLieeRepository demandeLieeRepository,
            TypeDemandeRepository typeDemandeRepository) {
        this.demandeRepository = demandeRepository;
        this.passeportRepository = passeportRepository;
        this.visaRepository = visaRepository;
        this.statutVisaRepository = statutVisaRepository;
        this.statutTitreTypeRepository = statutTitreTypeRepository;
        this.statutDemandeRepository = statutDemandeRepository;
        this.statutDemandeTypeRepository = statutDemandeTypeRepository;
        this.demandeLieeRepository = demandeLieeRepository;
        this.typeDemandeRepository = typeDemandeRepository;
    }

    @Transactional
    public Visa insererAncienVisa(
            Integer idDemande,
            Integer idPasseport,
            String reference,
            LocalDate dateDebut,
            LocalDate dateFin) {

        Demande demande = demandeRepository.findById(idDemande)
            .orElseThrow(() -> new EntityNotFoundException(
                "Demande non trouvee : " + idDemande));

        Passeport passeport = passeportRepository.findById(idPasseport)
            .orElseThrow(() -> new EntityNotFoundException(
                "Passeport non trouve : " + idPasseport));

        Visa ancienVisa = new Visa();
        ancienVisa.setDemande(demande);
        ancienVisa.setPasseport(passeport);
        ancienVisa.setReference(reference);
        ancienVisa.setDateDebut(dateDebut);
        ancienVisa.setDateFin(dateFin);
        Visa ancienVisaSauvegarde = visaRepository.save(ancienVisa);

        StatutTitreType statutPasseportExpire = statutTitreTypeRepository
            .findByLibelle("Passeport expire")
            .orElseThrow(() -> new EntityNotFoundException(
                "Statut Passeport expire non trouve"));

        StatutVisa statutVisa = new StatutVisa();
        statutVisa.setVisa(ancienVisaSauvegarde);
        statutVisa.setStatutType(statutPasseportExpire);
        statutVisa.setDateChangement(LocalDateTime.now());
        statutVisa.setCommentaire(
            "Ancien visa - passeport expire, transfert en cours");
        statutVisaRepository.save(statutVisa);

        log.info("Ancien visa insere id={} statut=Passeport expire",
            ancienVisaSauvegarde.getId());

        return ancienVisaSauvegarde;
    }

    @Transactional
    public Demande finaliserTransfert(
            Integer idDemandeOrigine,
            Visa ancienVisa,
            String numeroPasseport,
            LocalDate dateDelivrance,
            LocalDate dateExpiration,
            String paysDelivrance) {

        Demande demandeOrigine = demandeRepository.findById(idDemandeOrigine)
            .orElseThrow(() -> new EntityNotFoundException(
                "Demande origine non trouvee : " + idDemandeOrigine));

        // ÉTAPE 1 — INSERT nouveau Passeport
        Passeport nouveauPasseport = new Passeport();
        nouveauPasseport.setDemandeur(demandeOrigine.getDemandeur());
        nouveauPasseport.setNumeroPasseport(numeroPasseport);
        nouveauPasseport.setDateDelivrance(dateDelivrance);
        nouveauPasseport.setDateExpiration(dateExpiration);
        nouveauPasseport.setPaysDelivrance(paysDelivrance);
        Passeport nouveauPasseportSauvegarde =
            passeportRepository.save(nouveauPasseport);
        log.info("Nouveau passeport insere id={}",
            nouveauPasseportSauvegarde.getId());

        // ÉTAPE 2 — Changer statut demande origine → "Titre délivré" (id=5)
        StatutDemandeType titreDelivre = statutDemandeTypeRepository
            .findById(5)
            .orElseThrow(() -> new EntityNotFoundException(
                "Statut Titre delivre non trouve"));
        StatutDemande statutOrigine = new StatutDemande();
        statutOrigine.setDemande(demandeOrigine);
        statutOrigine.setStatutDemandeType(titreDelivre); // ← setter correct
        statutOrigine.setDateChangement(LocalDateTime.now());
        statutDemandeRepository.save(statutOrigine);
        log.info("Demande origine id={} passe a Titre delivre",
            idDemandeOrigine);

        // ÉTAPE 3 — INSERT nouvelle Demande (type=3 transfert)
        StatutDemandeType dossierCree = statutDemandeTypeRepository
            .findById(1)
            .orElseThrow(() -> new EntityNotFoundException(
                "Statut Dossier cree non trouve"));

        Demande nouvelleDemande = new Demande();
        nouvelleDemande.setDemandeur(demandeOrigine.getDemandeur());
        nouvelleDemande.setTypeVisa(demandeOrigine.getTypeVisa()); // ← setter correct
        nouvelleDemande.setTypeDemande(                             // ← setter correct
            typeDemandeRepository.findById(3)
                .orElseThrow(() -> new EntityNotFoundException(
                    "Type demande Transfert non trouve")));
        nouvelleDemande.setDateDemande(demandeOrigine.getDateDemande());
        nouvelleDemande.setVisaTransformable(
            demandeOrigine.getVisaTransformable());
        Demande nouvelledemandeSauvegardee =
            demandeRepository.save(nouvelleDemande);

        StatutDemande statutNouvelleDemande = new StatutDemande();
        statutNouvelleDemande.setDemande(nouvelledemandeSauvegardee);
        statutNouvelleDemande.setStatutDemandeType(dossierCree); // ← setter correct
        statutNouvelleDemande.setDateChangement(LocalDateTime.now());
        statutDemandeRepository.save(statutNouvelleDemande);
        log.info("Nouvelle demande transfert creee id={}",
            nouvelledemandeSauvegardee.getId());

        // ÉTAPE 4 — INSERT DemandeLiee
        DemandeLiee demandeLiee = new DemandeLiee();
        demandeLiee.setDemandeOrigine(demandeOrigine);
        demandeLiee.setDemandeLiee(nouvelledemandeSauvegardee);
        demandeLiee.setTypeLien("transfert");
        demandeLieeRepository.save(demandeLiee);
        log.info("DemandeLiee creee origine={} liee={} type=transfert",
            idDemandeOrigine, nouvelledemandeSauvegardee.getId());

        // ÉTAPE 5 — INSERT nouveau Visa (copie de l'ancien)
        Visa nouveauVisa = new Visa();
        nouveauVisa.setDemande(nouvelledemandeSauvegardee);
        nouveauVisa.setPasseport(nouveauPasseportSauvegarde);
        nouveauVisa.setReference(ancienVisa.getReference());
        nouveauVisa.setDateDebut(ancienVisa.getDateDebut());
        nouveauVisa.setDateFin(ancienVisa.getDateFin());
        Visa nouveauVisaSauvegarde = visaRepository.save(nouveauVisa);
            
        // ÉTAPE 6 — INSERT StatutVisa = "Actif"
        StatutTitreType actif = statutTitreTypeRepository
            .findByLibelle("Actif")
            .orElseThrow(() -> new EntityNotFoundException(
                "Statut Actif non trouve"));
        StatutVisa statutNouveauVisa = new StatutVisa();
        statutNouveauVisa.setVisa(nouveauVisaSauvegarde);
        statutNouveauVisa.setStatutType(actif);
        statutNouveauVisa.setDateChangement(LocalDateTime.now());
        statutNouveauVisa.setCommentaire("Nouveau visa - transfert effectue");
        statutVisaRepository.save(statutNouveauVisa);
        log.info("Nouveau visa cree id={} statut=Actif",
            nouveauVisaSauvegarde.getId());

        return nouvelledemandeSauvegardee;
    }

    public Map<String, String> validerAncienVisa(
            String reference,
            LocalDate dateDebut,
            LocalDate dateFin) {

        Map<String, String> erreurs = new LinkedHashMap<>();
        if (reference == null || reference.trim().isEmpty()) {
            erreurs.put("reference", "La reference est obligatoire.");
        }
        if (dateDebut == null) {
            erreurs.put("dateDebut", "La date de debut est obligatoire.");
        }
        if (dateFin == null) {
            erreurs.put("dateFin", "La date de fin est obligatoire.");
        }
        if (dateDebut != null && dateFin != null
                && !dateDebut.isBefore(dateFin)) {
            erreurs.put("dateFin",
                "La date de fin doit etre apres la date de debut.");
        }
        return erreurs;
    }

    public Map<String, String> validerNouveauPasseport(
            String numeroPasseport,
            LocalDate dateDelivrance,
            LocalDate dateExpiration,
            String paysDelivrance) {

        Map<String, String> erreurs = new LinkedHashMap<>();
        if (numeroPasseport == null || numeroPasseport.trim().isEmpty()) {
            erreurs.put("numeroPasseport",
                "Le numero de passeport est obligatoire.");
        }
        if (dateDelivrance == null) {
            erreurs.put("dateDelivrance",
                "La date de delivrance est obligatoire.");
        }
        if (dateExpiration == null) {
            erreurs.put("dateExpiration",
                "La date d'expiration est obligatoire.");
        }
        if (dateDelivrance != null && dateExpiration != null
                && !dateDelivrance.isBefore(dateExpiration)) {
            erreurs.put("dateExpiration",
                "La date d'expiration doit etre apres la date de delivrance.");
        }
        if (paysDelivrance == null || paysDelivrance.trim().isEmpty()) {
            erreurs.put("paysDelivrance",
                "Le pays de delivrance est obligatoire.");
        }
        return erreurs;
    }
}