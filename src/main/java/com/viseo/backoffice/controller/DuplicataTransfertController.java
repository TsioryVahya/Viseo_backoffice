package com.viseo.backoffice.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.viseo.backoffice.model.Demande;
import com.viseo.backoffice.model.Demandeur;
import com.viseo.backoffice.model.Nationalite;
import com.viseo.backoffice.model.SituationFamiliale;
import com.viseo.backoffice.model.TypeDemande;
import com.viseo.backoffice.model.TypeVisa;
import com.viseo.backoffice.service.DemandeurService;
import com.viseo.backoffice.service.NationaliteService;
import com.viseo.backoffice.service.SituationFamilialeService;
import com.viseo.backoffice.service.Sprint2Service;
import com.viseo.backoffice.service.TypeDemandeService;
import com.viseo.backoffice.service.TypeVisaService;

import jakarta.servlet.http.HttpSession;

/**
 * Contrôleur pour la gestion des demandes de Duplicata et Transfert (Sprint 2)
 * Permet de rechercher un demandeur existant ou en créer un nouveau avec approbation immédiate
 */
@Controller
@RequestMapping("/demande/duplicata-transfert")
public class DuplicataTransfertController {

    private static final String SESSION_DEMANDEUR = "demandeurSession";
    private static final String SESSION_DEMANDE = "demandeSession";

    private final DemandeurService demandeurService;
    private final TypeDemandeService typeDemandeService;
    private final Sprint2Service sprint2Service;
    private final SituationFamilialeService situationFamilialeService;
    private final NationaliteService nationaliteService;
    private final TypeVisaService typeVisaService;

    public DuplicataTransfertController(
            DemandeurService demandeurService,
            TypeDemandeService typeDemandeService,
            Sprint2Service sprint2Service,
            SituationFamilialeService situationFamilialeService,
            NationaliteService nationaliteService,
            TypeVisaService typeVisaService) {
        this.demandeurService = demandeurService;
        this.typeDemandeService = typeDemandeService;
        this.sprint2Service = sprint2Service;
        this.situationFamilialeService = situationFamilialeService;
        this.nationaliteService = nationaliteService;
        this.typeVisaService = typeVisaService;
    }

    /**
     * Affiche la page de recherche pour duplicata/transfert
     */
    @GetMapping("/recherche")
    public String recherche(Model model) {
        // Préparer les types de demande disponibles (Duplicata et Transfert)
        List<TypeDemande> typesDemande = typeDemandeService.findAll();
        List<TypeDemande> typesFiltrés = typesDemande.stream()
                .filter(sprint2Service::isDuplicataOuTransfert)
                .toList();
        
        model.addAttribute("typesDemande", typesFiltrés);
        return "demande/duplicata-transfert/recherche";
    }

    /**
     * Vérifie si un demandeur existe par nom et prénom
     * - Si trouvé : affiche ses infos et permet de confirmer
     * - Si non trouvé : préremplit l'étape 1 du nouveau titre avec nom/prénom en session
     */
    @PostMapping("/verifier")
    public String verifier(
            @RequestParam String nom,
            @RequestParam String prenom,
            @RequestParam Integer idTypeDemande,
            Model model,
            HttpSession session) {
        
        // Rechercher le demandeur par nom et prénom
        List<Demandeur> demandeurs = demandeurService.searchByNomAndPrenom(nom, prenom);
        
        if (!demandeurs.isEmpty()) {
            // Demandeur trouvé : afficher ses infos
            Demandeur demandeur = demandeurs.get(0); // Prendre le premier résultat
            
            // Récupérer le type de demande sélectionné
            TypeDemande typeDemande = typeDemandeService.findById(idTypeDemande)
                    .orElse(null);
            
            model.addAttribute("demandeur", demandeur);
            model.addAttribute("typeDemande", typeDemande);
            model.addAttribute("idTypeDemande", idTypeDemande);
            // Passer les IDs des relations pour éviter les problèmes de lazy loading
            model.addAttribute("idSituationFamiliale", demandeur.getSituationFamiliale().getId());
            model.addAttribute("idNationalite", demandeur.getNationalite().getId());
            // Passer les types de visa disponibles
            model.addAttribute("typesVisa", typeVisaService.findAll());
            
            return "demande/duplicata-transfert/confirmation";
        } else {
            // Demandeur non trouvé : démarrer le parcours nouveau titre avec un pré-remplissage minimal.
            Demandeur nouveauDemandeur = new Demandeur();
            nouveauDemandeur.setNom(nom == null ? "" : nom.trim());
            nouveauDemandeur.setPrenom(prenom == null ? "" : prenom.trim());
            session.setAttribute(SESSION_DEMANDEUR, nouveauDemandeur);

            Demande nouvelleDemande = new Demande();
            nouvelleDemande.setDemandeur(nouveauDemandeur);
            nouvelleDemande.setDateDemande(LocalDate.now());
            typeDemandeService.findById(idTypeDemande).ifPresent(nouvelleDemande::setTypeDemande);
            session.setAttribute(SESSION_DEMANDE, nouvelleDemande);

            return "redirect:/demande/etape1";
        }
    }

    /**
     * Affiche le formulaire pour créer un nouveau demandeur (duplicata/transfert)
     */
    @GetMapping("/formulaire")
    public String formulaire(
            @RequestParam(required = false) Integer idTypeDemande,
            Model model) {
        
        try {
            // Préparer les types de demande disponibles (Duplicata et Transfert seulement)
            List<TypeDemande> typesDemande = typeDemandeService.findAll();
            List<TypeDemande> typesFiltrés = typesDemande.stream()
                    .filter(sprint2Service::isDuplicataOuTransfert)
                    .toList();
            
            // Charger les données de référence obligatoires
            List<SituationFamiliale> situationsFamiliales = situationFamilialeService.findAll();
            List<Nationalite> nationalites = nationaliteService.findAll();
            List<TypeVisa> typesVisa = typeVisaService.findAll();
            
            // Préparer un nouveau demandeur vide pour le formulaire
            Demandeur demandeur = new Demandeur();
            
            model.addAttribute("demandeur", demandeur);
            model.addAttribute("typesDemande", typesFiltrés);
            model.addAttribute("situationsFamiliales", situationsFamiliales);
            model.addAttribute("nationalites", nationalites);
            model.addAttribute("typesVisa", typesVisa);
            
            if (idTypeDemande != null) {
                TypeDemande typeDemande = typeDemandeService.findById(idTypeDemande).orElse(null);
                model.addAttribute("typeDemande", typeDemande);
                model.addAttribute("idTypeDemande", idTypeDemande);
            }
            
        } catch (Exception e) {
            model.addAttribute("erreur", "Erreur lors du chargement du formulaire: " + e.getMessage());
        }
        
        return "demande/duplicata-transfert/formulaire";
    }

    /**
     * Valide et enregistre la demande de duplicata/transfert
     * Appelle le Sprint2Service pour :
     * - Créer/récupérer le demandeur
     * - Créer la demande
     * - Mettre le statut à 'Titre délivré' immédiatement
     */
    @PostMapping("/valider")
    public String valider(
            @RequestParam(required = false) Integer id,
            @RequestParam String nom,
            @RequestParam String prenom,
            @RequestParam String dateNaissance,
            @RequestParam String lieuNaissance,
            @RequestParam String telephone,
            @RequestParam String email,
            @RequestParam String adresse,
            @RequestParam Integer idTypeDemande,
            @RequestParam Integer idSituationFamiliale,
            @RequestParam Integer idNationalite,
            @RequestParam(required = false) Integer idTypeVisa,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        try {
            // Construire l'objet Demandeur
            Demandeur demandeur = new Demandeur();
            if (id != null && id > 0) {
                demandeur.setId(id);
            }
            demandeur.setNom(nom);
            demandeur.setPrenom(prenom);
            demandeur.setDateNaissance(java.time.LocalDate.parse(dateNaissance));
            demandeur.setLieuNaissance(lieuNaissance);
            demandeur.setTelephone(telephone);
            demandeur.setEmail(email);
            demandeur.setAdresse(adresse);
            
            // Appeler le service Sprint2 pour traiter la demande
            Demande demandeCreee = sprint2Service.traiterDuplicataTransfert(
                    demandeur,
                    idTypeDemande,
                    idSituationFamiliale,
                    idNationalite,
                    idTypeVisa);
            
            // Afficher la confirmation avec la demande créée
            redirectAttributes.addFlashAttribute("message", 
                    "Demande de duplicata/transfert créée avec succès et approuvée !");
            redirectAttributes.addFlashAttribute("demande", demandeCreee);
            
            return "redirect:/demande/duplicata-transfert/confirmation-finale";
            
        } catch (IllegalArgumentException e) {
            // Erreur de validation
            redirectAttributes.addFlashAttribute("erreur", 
                    "Erreur : " + e.getMessage());
            
            return "redirect:/demande/duplicata-transfert/formulaire?idTypeDemande=" + idTypeDemande;
        } catch (Exception e) {
            // Erreur système
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("erreur", 
                    "Une erreur est survenue lors de l'enregistrement : " + e.getMessage());
            
            return "redirect:/demande/duplicata-transfert/formulaire?idTypeDemande=" + idTypeDemande;
        }
    }

    /**
     * Affiche la confirmation finale après approbation
     */
    @GetMapping("/confirmation-finale")
    public String confirmationFinale(Model model) {
        return "demande/duplicata-transfert/confirmation-finale";
    }
}
