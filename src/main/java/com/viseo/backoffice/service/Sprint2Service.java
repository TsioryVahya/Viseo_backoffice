package com.viseo.backoffice.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.viseo.backoffice.model.Demande;
import com.viseo.backoffice.model.Demandeur;
import com.viseo.backoffice.model.Nationalite;
import com.viseo.backoffice.model.SituationFamiliale;
import com.viseo.backoffice.model.StatutDemande;
import com.viseo.backoffice.model.StatutDemandeType;
import com.viseo.backoffice.model.TypeDemande;
import com.viseo.backoffice.model.TypeVisa;

/**
 * Service dédié aux fonctionnalités du Sprint 2
 * Gère la création et l'approbation immédiate des duplicatas et transferts
 */
@Service
public class Sprint2Service {

    private static final Logger logger = LoggerFactory.getLogger(Sprint2Service.class);

    private final DemandeurService demandeurService;
    private final DemandeService demandeService;
    private final TypeDemandeService typeDemandeService;
    private final StatutDemandeService statutDemandeService;
    private final StatutDemandeTypeService statutDemandeTypeService;
    private final SituationFamilialeService situationFamilialeService;
    private final NationaliteService nationaliteService;
    private final TypeVisaService typeVisaService;

    public Sprint2Service(
            DemandeurService demandeurService,
            DemandeService demandeService,
            TypeDemandeService typeDemandeService,
            StatutDemandeService statutDemandeService,
            StatutDemandeTypeService statutDemandeTypeService,
            SituationFamilialeService situationFamilialeService,
            NationaliteService nationaliteService,
            TypeVisaService typeVisaService) {
        this.demandeurService = demandeurService;
        this.demandeService = demandeService;
        this.typeDemandeService = typeDemandeService;
        this.statutDemandeService = statutDemandeService;
        this.statutDemandeTypeService = statutDemandeTypeService;
        this.situationFamilialeService = situationFamilialeService;
        this.nationaliteService = nationaliteService;
        this.typeVisaService = typeVisaService;
    }

    /**
     * Traite la création d'une demande de Duplicata ou Transfert avec approbation immédiate
     * 
     * Logique :
     * 1. Si le demandeur n'existe pas : créer le Demandeur en base
     * 2. Créer une nouvelle Demande liée au demandeur
     * 3. Attribuer le type de demande (Duplicata ou Transfert) - jamais "Nouveau Titre"
     * 4. Créer une entrée StatutDemande avec le type "Titre délivré" et la date actuelle
     * 5. Retourner l'objet Demande créé
     * 
     * @param demandeur le Demandeur (peut être nouveau ou existant)
     * @param idTypeDemande l'ID du TypeDemande (Duplicata ou Transfert)
     * @param idSituationFamiliale l'ID de la situation familiale
     * @param idNationalite l'ID de la nationalité
     * @param idTypeVisa l'ID du type de visa (optionnel, peut être null)
     * @return la Demande créée avec statut approuvé
     * @throws IllegalArgumentException si les données sont invalides
     */
    public Demande traiterDuplicataTransfert(
            Demandeur demandeur,
            Integer idTypeDemande,
            Integer idSituationFamiliale,
            Integer idNationalite,
            Integer idTypeVisa) {
        
        // 1. Charger et vérifier les objets de référence obligatoires
        SituationFamiliale situationFamiliale = situationFamilialeService.findById(idSituationFamiliale)
                .orElseThrow(() -> new IllegalArgumentException("Situation familiale non trouvée avec l'ID : " + idSituationFamiliale));
        
        Nationalite nationalite = nationaliteService.findById(idNationalite)
                .orElseThrow(() -> new IllegalArgumentException("Nationalité non trouvée avec l'ID : " + idNationalite));
        
        // 2. Charger le type de visa (optionnel pour duplicata/transfert)
        TypeVisa typeVisa = null;
        if (idTypeVisa != null && idTypeVisa > 0) {
            typeVisa = typeVisaService.findById(idTypeVisa)
                    .orElse(null);
        }
        
        // 3. Vérifier/créer le Demandeur
        Demandeur demandeurSaved;
        if (demandeur.getId() != null && demandeur.getId() > 0) {
            // Le demandeur existe déjà : on ne crée pas de doublon
            Optional<Demandeur> existant = demandeurService.findById(demandeur.getId());
            demandeurSaved = existant.orElse(demandeur);
        } else {
            // Créer un nouveau demandeur avec les références obligatoires
            demandeur.setSituationFamiliale(situationFamiliale);
            demandeur.setNationalite(nationalite);
            demandeurSaved = demandeurService.save(demandeur);
        }

        // 4. Vérifier que le type de demande est valide
        Optional<TypeDemande> typeDemande = typeDemandeService.findOptionalById(idTypeDemande);
        if (typeDemande.isEmpty()) {
            throw new IllegalArgumentException("Type de demande non trouvé avec l'ID : " + idTypeDemande);
        }

        // Vérifier que ce n'est pas "Nouveau Titre"
        String libelle = typeDemande.get().getLibelle();
        if ("Nouveau Titre".equalsIgnoreCase(libelle)) {
            throw new IllegalArgumentException("Le type 'Nouveau Titre' ne peut pas être traité comme duplicata/transfert");
        }

        // 5. Créer une nouvelle Demande
        Demande demande = new Demande();
        demande.setDemandeur(demandeurSaved);
        demande.setTypeDemande(typeDemande.get());
        demande.setTypeVisa(typeVisa);
        demande.setDateDemande(LocalDate.now());
        
        Demande demandeSaved = demandeService.save(demande);

        // 6. Créer une entrée StatutDemande avec le type "Titre délivré"
        logger.info("Recherche du statut 'Titre délivré'...");
        
        // D'abord, afficher tous les statuts disponibles
        List<StatutDemandeType> tousLesStatuts = statutDemandeTypeService.findAll();
        logger.info("Statuts disponibles en base:");
        for (StatutDemandeType s : tousLesStatuts) {
            logger.info("  - ID: {}, Libellé: '{}'", s.getId(), s.getLibelle());
        }
        
        // Chercher le statut "Titre délivré"
        Optional<Integer> idStatutTitreDelivre = statutDemandeTypeService.getIdStatutTitreDelivre();
        logger.info("Résultat de la recherche 'Titre délivré': {}", idStatutTitreDelivre);
        
        if (idStatutTitreDelivre.isEmpty()) {
            // Si la recherche échoue, essayer de récupérer l'ID 5 directement
            logger.warn("Statut 'Titre délivré' non trouvé par libellé, essai avec ID 5...");
            Optional<StatutDemandeType> statut5 = statutDemandeTypeService.findById(5);
            if (statut5.isPresent()) {
                logger.info("Statut trouvé avec ID 5 : '{}'", statut5.get().getLibelle());
                idStatutTitreDelivre = Optional.of(5);
            } else {
                throw new IllegalArgumentException("Le statut 'Titre délivré' n'existe pas en base de données");
            }
        }

        Optional<StatutDemandeType> statutType = statutDemandeTypeService.findById(idStatutTitreDelivre.get());
        if (statutType.isEmpty()) {
            throw new IllegalArgumentException("Impossible de récupérer le statut 'Titre délivré'");
        }

        StatutDemande statutDemande = new StatutDemande();
        statutDemande.setDemande(demandeSaved);
        statutDemande.setStatutDemandeType(statutType.get());
        statutDemande.setDateChangement(LocalDateTime.now());
        
        statutDemandeService.save(statutDemande);

        // 7. Retourner la Demande créée
        return demandeSaved;
    }

    /**
     * Traite la création d'une demande de Duplicata ou Transfert avec approbation immédiate (surcharge)
     * Version simplifiée qui récupère les IDs depuis les objets
     * 
     * @param demandeur le Demandeur
     * @param idTypeDemande l'ID du TypeDemande
     * @return la Demande créée avec statut approuvé
     */
    public Demande traiterDuplicataTransfert(Demandeur demandeur, Integer idTypeDemande) {
        // Version de compatibilité - utilise null pour les autres paramètres
        return traiterDuplicataTransfert(demandeur, idTypeDemande, null, null, null);
    }

    /**
     * Valide que le type de demande est un duplicata ou un transfert
     * 
     * @param typeDemande le type de demande à valider
     * @return true si c'est un duplicata ou transfert, false sinon
     */
    public boolean isDuplicataOuTransfert(TypeDemande typeDemande) {
        if (typeDemande == null || typeDemande.getLibelle() == null) {
            return false;
        }
        String libelle = typeDemande.getLibelle().toLowerCase();
        return libelle.contains("duplicata") || libelle.contains("transfert");
    }
}
