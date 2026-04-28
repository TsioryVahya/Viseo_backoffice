package com.viseo.backoffice.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.viseo.backoffice.model.CarteResident;
import com.viseo.backoffice.model.Demande;
import com.viseo.backoffice.model.Demandeur;
import com.viseo.backoffice.model.Passeport;
import com.viseo.backoffice.model.PieceDemande;
import com.viseo.backoffice.model.PieceDemandeSpecifique;
import com.viseo.backoffice.model.TypeDemande;
import com.viseo.backoffice.model.TypePieceCommune;
import com.viseo.backoffice.model.TypePieceSpecifique;
import com.viseo.backoffice.model.UploadPiece;
import com.viseo.backoffice.model.Visa;
import com.viseo.backoffice.service.*;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/duplicata")
public class DuplicataController {

    private static final Logger log = LoggerFactory.getLogger(DuplicataController.class);

    private final DuplicataService duplicataService;
    private final TypeDemandeService typeDemandeService;
    private final TransfertService transfertService;

    public DuplicataController(DuplicataService duplicataService, TypeDemandeService typeDemandeService, TransfertService transfertService) {
        this.duplicataService = duplicataService;
        this.typeDemandeService = typeDemandeService;
        this.transfertService = transfertService;
    }

    @GetMapping("/recherche")
    public String recherche(Model model) {
        model.addAttribute("typesDemande", typeDemandeService.findAllSaufNouveauTitre());
        model.addAttribute("menuActif", "duplicata");
        return "duplicata/recherche";
    }

    @PostMapping("/recherche")
    public String traiterRecherche(
            @RequestParam String nom,
            @RequestParam String prenom,
            @RequestParam String numeroPasseport,
            @RequestParam(required = false) Integer typeDemandeId,
            HttpSession session,
            Model model) {  

        Map<String, String> erreurs =
            duplicataService.validerRecherche(nom, prenom, numeroPasseport);
        if (typeDemandeId == null) {
            erreurs.put("typeDemandeId", "Veuillez choisir un type de demande.");
        }
        if (!erreurs.isEmpty()) {
            model.addAttribute("erreurs", erreurs);
            model.addAttribute("typesDemande",
                typeDemandeService.findAllSaufNouveauTitre());
            model.addAttribute("menuActif", "duplicata");
            return "duplicata/recherche";
        }

        TypeDemande typeDemande = typeDemandeService.findById(typeDemandeId);
        session.setAttribute("typeDemandeId", typeDemandeId);
        session.setAttribute("typeDemandeLibelle", typeDemande.getLibelle());

        Optional<Demandeur> demandeur =
            duplicataService.rechercherDemandeur(nom, prenom, numeroPasseport);
        Optional<Passeport> passeport =
            duplicataService.findPasseportByNumero(numeroPasseport);

        // ══════════════════════════════════════
        // CAS TROUVÉ
        // ══════════════════════════════════════
        if (demandeur.isPresent() && passeport.isPresent()) {
            session.setAttribute("demandeurTrouve", true);
            session.setAttribute("idDemandeurExistant",
                demandeur.get().getId());
            session.setAttribute("idPasseportExistant",
                passeport.get().getId());
            log.info("Demandeur trouve - id={} typedemande={}",
                demandeur.get().getId(), typeDemande.getLibelle());

            if ("Duplicata".equals(typeDemande.getLibelle())) {
                return "redirect:/duplicata/demandeur-trouve";
            }
            if ("Transfert de visa".equals(typeDemande.getLibelle())) {
                // À implémenter plus tard
                model.addAttribute("info",
                    "Transfert pour demandeur existant : " +
                    "en cours de developpement.");
                model.addAttribute("typesDemande",
                    typeDemandeService.findAllSaufNouveauTitre());
                model.addAttribute("menuActif", "renouvellement");
                return "duplicata/recherche";
            }
        }

        // ══════════════════════════════════════
        // CAS NON TROUVÉ — même route pour les deux
        // ══════════════════════════════════════
        session.setAttribute("demandeurTrouve", false);
        log.info("Demandeur non trouve - demarrage flux complet typedemande={}",
            typeDemande.getLibelle());

        if ("Duplicata".equals(typeDemande.getLibelle())
                || "Transfert de visa".equals(typeDemande.getLibelle())) {
            return "redirect:/duplicata/etape1"; // ← même route pour les deux
        }

        // Fallback sécurité
        model.addAttribute("info",
            "Ce type de demande est en cours de developpement.");
        model.addAttribute("typesDemande",
            typeDemandeService.findAllSaufNouveauTitre());
        model.addAttribute("menuActif", "renouvellement");
        return "duplicata/recherche";
    }

    @GetMapping("/demandeur-trouve")
    public String demandeurTrouve(HttpSession session, Model model) {
        Object dateDemande = session.getAttribute("dateDemandeTrouve");
        model.addAttribute("dateDemande", dateDemande);
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("menuActif", "renouvellement");
        return "duplicata/demandeur_trouve";
    }

    @PostMapping("/demandeur-trouve")
    public String traiterDemandeurTrouve(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDemande,
            @RequestParam(required = false) Boolean cocheVisa,
            @RequestParam(required = false) Boolean cocheCarte,
            @RequestParam(required = false) String referenceVisa,
            @RequestParam(required = false) String referenceCarte,
            HttpSession session,
            Model model) {
        Map<String, String> erreurs = duplicataService.validerDemandeurTrouve(
                dateDemande,
                cocheVisa,
                cocheCarte,
                referenceVisa,
                referenceCarte);
        if (!erreurs.isEmpty()) {
            model.addAttribute("erreurs", erreurs);
            model.addAttribute("dateDemande", dateDemande);
            model.addAttribute("today", LocalDate.now());
            model.addAttribute("menuActif", "renouvellement");
            return "duplicata/demandeur_trouve";
        }

        try {
            session.setAttribute("dateDemandeTrouve", dateDemande);
            Demande nouvelleDemande = duplicataService.traiterDemandeurTrouve(
                    (Integer) session.getAttribute("idDemandeurExistant"),
                    (Integer) session.getAttribute("idPasseportExistant"),
                    (Integer) session.getAttribute("typeDemandeId"),
                    dateDemande,
                    cocheVisa != null && cocheVisa,
                    cocheCarte != null && cocheCarte,
                    referenceVisa,
                    referenceCarte);

            log.info("Duplicata trouve finalise - nouvelleDemande={}", nouvelleDemande.getId());
            session.invalidate();
            return "redirect:/demande/confirmation/" + nouvelleDemande.getId();
        } catch (Exception e) {
            log.error("Erreur traitement demandeur trouve", e);
            model.addAttribute("erreur", "Erreur : " + e.getMessage());
            model.addAttribute("dateDemande", dateDemande);
            model.addAttribute("today", LocalDate.now());
            model.addAttribute("menuActif", "renouvellement");
            return "duplicata/demandeur_trouve";
        }
    }

    @GetMapping("/etape1")
    public String etape1(HttpSession session, Model model) {
        model.addAttribute("nationalites", duplicataService.findAllNationalites());
        model.addAttribute("situationsFamiliales", duplicataService.findAllSituationsFamiliales());
        model.addAttribute("demandeur", session.getAttribute("demandeur"));
        model.addAttribute("menuActif", "duplicata");
        model.addAttribute("etapeActuelle", 1);
        return "duplicata/etape1";
    }

    @PostMapping("/etape1")
    public String traiterEtape1(
            @RequestParam String nom,
            @RequestParam String prenom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateNaissance,
            @RequestParam String lieuNaissance,
            @RequestParam String telephone,
            @RequestParam String email,
            @RequestParam String adresse,
            @RequestParam(required = false) Integer idNationalite,
            @RequestParam(required = false) Integer idSituationFamiliale,
            HttpSession session,
            Model model) {
        Map<String, String> erreurs = duplicataService.validerDemandeur(
                nom,
                prenom,
                dateNaissance,
                lieuNaissance,
                telephone,
                email,
                adresse,
                idNationalite,
                idSituationFamiliale);
        if (!erreurs.isEmpty()) {
            model.addAttribute("erreurs", erreurs);
            model.addAttribute("nationalites", duplicataService.findAllNationalites());
            model.addAttribute("situationsFamiliales", duplicataService.findAllSituationsFamiliales());
            model.addAttribute("menuActif", "duplicata");
            model.addAttribute("etapeActuelle", 1);
            return "duplicata/etape1";
        }

        Demandeur demandeur = duplicataService.construireDemandeur(
                nom,
                prenom,
                dateNaissance,
                lieuNaissance,
                telephone,
                email,
                adresse,
                idNationalite,
                idSituationFamiliale);
        session.setAttribute("demandeur", demandeur);
        return "redirect:/duplicata/etape2";
    }

    @GetMapping("/etape2")
    public String etape2(HttpSession session, Model model) {
        model.addAttribute("passeportData", session.getAttribute("passeportData"));
        model.addAttribute("numeroReferenceVisa", session.getAttribute("numeroReferenceVisa"));
        model.addAttribute("dateExpirationVisa", session.getAttribute("dateExpirationVisa"));
        model.addAttribute("menuActif", "duplicata");
        model.addAttribute("etapeActuelle", 2);
        return "duplicata/etape2";
    }

    @PostMapping("/etape2")
    public String traiterEtape2(
            @RequestParam String numeroPasseport,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDelivrance,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateExpiration,
            @RequestParam String paysDelivrance,
            @RequestParam String numeroReferenceVisa,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateExpirationVisa,
            HttpSession session,
            Model model) {
        Map<String, String> erreurs = duplicataService.validerEtape2(
                numeroPasseport,
                dateDelivrance,
                dateExpiration,
                paysDelivrance,
                numeroReferenceVisa,
                dateExpirationVisa);
        if (!erreurs.isEmpty()) {
            model.addAttribute("erreurs", erreurs);
            model.addAttribute("menuActif", "duplicata");
            model.addAttribute("etapeActuelle", 2);
            return "duplicata/etape2";
        }

        Map<String, Object> passeportData = new HashMap<>();
        passeportData.put("numero", numeroPasseport);
        passeportData.put("dateDelivrance", dateDelivrance);
        passeportData.put("dateExpiration", dateExpiration);
        passeportData.put("paysDelivrance", paysDelivrance);
        session.setAttribute("passeportData", passeportData);
        session.setAttribute("numeroReferenceVisa", numeroReferenceVisa);
        session.setAttribute("dateExpirationVisa", dateExpirationVisa);
        return "redirect:/duplicata/etape3a";
    }

    @GetMapping("/etape3a")
    public String etape3a(HttpSession session, Model model) {
        Object dateDemande = session.getAttribute("dateDemande");
        model.addAttribute("typesVisa", duplicataService.findAllTypesVisa());
        model.addAttribute("piecesCommunes", duplicataService.findAllPiecesCommunes());
        model.addAttribute("typeVisaId", session.getAttribute("typeVisaId"));
        model.addAttribute("dateDemande", dateDemande != null ? dateDemande : LocalDate.now());
        model.addAttribute("piecesChecked", session.getAttribute("piecesCommunes"));
        model.addAttribute("menuActif", "duplicata");
        model.addAttribute("etapeActuelle", 3);
        return "duplicata/etape3a";
    }

    @PostMapping("/etape3a")
    public String traiterEtape3a(
            @RequestParam(required = false) Integer typeVisaId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDemande,
            HttpServletRequest request,
            HttpSession session,
            Model model) {
        List<TypePieceCommune> piecesRef = duplicataService.findAllPiecesCommunes();
        Map<Integer, Boolean> piecesCommunes = new HashMap<>();
        for (TypePieceCommune piece : piecesRef) {
            piecesCommunes.put(piece.getId(), request.getParameter("piece_" + piece.getId()) != null);
        }

        LocalDate dateExpirationVisa = (LocalDate) session.getAttribute("dateExpirationVisa");
        Map<String, String> erreurs = duplicataService.validerEtape3a(
                typeVisaId,
                dateDemande,
                dateExpirationVisa,
                piecesCommunes,
                piecesRef);
        if (!erreurs.isEmpty()) {
            model.addAttribute("erreurs", erreurs);
            model.addAttribute("typesVisa", duplicataService.findAllTypesVisa());
            model.addAttribute("piecesCommunes", piecesRef);
            model.addAttribute("typeVisaId", typeVisaId);
            model.addAttribute("dateDemande", dateDemande);
            model.addAttribute("piecesChecked", piecesCommunes);
            model.addAttribute("menuActif", "duplicata");
            model.addAttribute("etapeActuelle", 3);
            return "duplicata/etape3a";
        }

        session.setAttribute("typeVisaId", typeVisaId);
        session.setAttribute("dateDemande", dateDemande);
        session.setAttribute("piecesCommunes", piecesCommunes);
        return "redirect:/duplicata/etape3b";
    }

    @GetMapping("/etape3b")
    public String etape3b(HttpSession session, Model model) {
        Integer typeVisaId = (Integer) session.getAttribute("typeVisaId");
        model.addAttribute("piecesSpecifiques", duplicataService.findPiecesSpecifiquesByTypeVisa(typeVisaId));
        model.addAttribute("typeVisaId", typeVisaId);
        model.addAttribute("typeVisaLibelle", duplicataService.findTypeVisaLibelleById(typeVisaId));
        model.addAttribute("piecesChecked", session.getAttribute("piecesSpecifiques"));
        model.addAttribute("menuActif", "duplicata");
        model.addAttribute("etapeActuelle", 3);
        return "duplicata/etape3b";
    }

    @PostMapping("/etape3b")
    public String traiterEtape3b(HttpServletRequest request, HttpSession session, Model model) {
        Integer typeVisaId = (Integer) session.getAttribute("typeVisaId");
        List<TypePieceSpecifique> toutesSpecifiques = duplicataService.findPiecesSpecifiquesByTypeVisa(typeVisaId);

        Map<Integer, Boolean> piecesSpecifiques = new HashMap<>();
        for (TypePieceSpecifique piece : toutesSpecifiques) {
            piecesSpecifiques.put(piece.getId(), request.getParameter("piece_" + piece.getId()) != null);
        }

        Map<String, String> erreurs = duplicataService.validerPiecesSpecifiques(piecesSpecifiques, toutesSpecifiques);
        if (!erreurs.isEmpty()) {
            log.warn("Pieces specifiques obligatoires manquantes typeVisa={}", typeVisaId);
            model.addAttribute("erreurs", erreurs);
            model.addAttribute("piecesSpecifiques", toutesSpecifiques);
            model.addAttribute("typeVisaId", typeVisaId);
            model.addAttribute("typeVisaLibelle", duplicataService.findTypeVisaLibelleById(typeVisaId));
            model.addAttribute("piecesChecked", piecesSpecifiques);
            model.addAttribute("menuActif", "duplicata");
            model.addAttribute("etapeActuelle", 3);
            return "duplicata/etape3b";
        }

        session.setAttribute("piecesSpecifiques", piecesSpecifiques);

        @SuppressWarnings("unchecked")
        Demande demande = duplicataService.creerDemandeEnCoursDeSaisie(
                (Demandeur) session.getAttribute("demandeur"),
                (Map<String, Object>) session.getAttribute("passeportData"),
                (Integer) session.getAttribute("typeVisaId"),
                (LocalDate) session.getAttribute("dateDemande"),
                (String) session.getAttribute("numeroReferenceVisa"),
                (LocalDate) session.getAttribute("dateExpirationVisa"),
                (Map<Integer, Boolean>) session.getAttribute("piecesCommunes"),
                piecesSpecifiques);

        session.setAttribute("idDemandeNouveauTitre", demande.getId());
        session.setAttribute("idPasseportSauvegarde", duplicataService.findPasseportByDemande(demande).getId());

        return "redirect:/duplicata/etape4";
    }

    @GetMapping("/etape4")
    public String etape4(HttpSession session, Model model) {
        Integer idDemande = (Integer) session.getAttribute("idDemandeNouveauTitre");
        Demande demande = duplicataService.findDemandeById(idDemande);
        List<PieceDemande> communes = duplicataService.findPiecesCommunesByDemande(demande);
        List<PieceDemandeSpecifique> specifiques = duplicataService.findPiecesSpecifiquesByDemande(demande);
        Map<Integer, UploadPiece> uploadsCommunes = duplicataService.getDerniersUploadsCommunes(communes);
        Map<Integer, UploadPiece> uploadsSpecifiques = duplicataService.getDerniersUploadsSpecifiques(specifiques);
        boolean peutContinuer = duplicataService.peutContinuerApresUpload(demande);

        model.addAttribute("demande", demande);
        model.addAttribute("piecesCommunes", communes);
        model.addAttribute("piecesSpecifiques", specifiques);
        model.addAttribute("derniersUploadsCommunes", uploadsCommunes);
        model.addAttribute("derniersUploadsSpecifiques", uploadsSpecifiques);
        model.addAttribute("peutContinuer", peutContinuer);
        model.addAttribute("today", LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
        model.addAttribute("menuActif", "duplicata");
        model.addAttribute("etapeActuelle", 4);
        return "duplicata/etape4_upload";
    }

    @PostMapping("/etape4/piece")
    public String uploadEtape4(
            @RequestParam Integer idPiece,
            @RequestParam String typePiece,
            @RequestParam MultipartFile fichier,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateUpload,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        Integer idDemande = (Integer) session.getAttribute("idDemandeNouveauTitre");
        try {
            duplicataService.traiterUpload(idDemande, idPiece, typePiece, fichier, dateUpload);
            redirectAttributes.addFlashAttribute("succes", "Fichier uploade avec succes.");
        } catch (Exception e) {
            log.error("Erreur upload duplicata piece={} demande={}", idPiece, idDemande, e);
            redirectAttributes.addFlashAttribute("erreur", "Erreur upload : " + e.getMessage());
        }
        return "redirect:/duplicata/etape4";
    }

    @GetMapping("/etape5")
    public String etape5(
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        Integer idDemande =
            (Integer) session.getAttribute("idDemandeNouveauTitre");
        Demande demande = duplicataService.findDemandeById(idDemande);

        List<String> manquantes =
            duplicataService.getPiecesObligatoiresNonUploadees(demande);
        if (!manquantes.isEmpty()) {
            log.warn("Acces etape5 bloque - manquantes={}", manquantes);
            redirectAttributes.addFlashAttribute("erreur",
                "Uploadez toutes les pieces obligatoires avant de continuer.");
            return "redirect:/duplicata/etape4";
        }

        String typeDemandeLibelle =
            (String) session.getAttribute("typeDemandeLibelle");

        // ← Point de divergence
        if ("Transfert de visa".equals(typeDemandeLibelle)) {
            model.addAttribute("reference",
                session.getAttribute("referenceAncienVisa"));
            model.addAttribute("dateDebut",
                session.getAttribute("dateDebutAncienVisa"));
            model.addAttribute("dateFin",
                session.getAttribute("dateFinAncienVisa"));
            model.addAttribute("menuActif", "renouvellement");
            model.addAttribute("etapeActuelle", 5);
            return "transfert/etape5_ancien_visa"; // ← JSP transfert
        }

        // Duplicata — comportement existant
        model.addAttribute("reference",
            session.getAttribute("referenceNouveauTitre"));
        model.addAttribute("dateDebut",
            session.getAttribute("dateDebutNouveauTitre"));
        model.addAttribute("dateFin",
            session.getAttribute("dateFinNouveauTitre"));
        model.addAttribute("menuActif", "duplicata");
        model.addAttribute("etapeActuelle", 5);
        return "duplicata/etape5"; // ← JSP duplicata
    }

    @PostMapping("/etape5")
    public String traiterEtape5(
            @RequestParam String reference,
            @RequestParam(required = false)
                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                LocalDate dateDebut,
            @RequestParam(required = false)
                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                LocalDate dateFin,
            HttpSession session,
            Model model) {

        String typeDemandeLibelle =
            (String) session.getAttribute("typeDemandeLibelle");

        if ("Transfert de visa".equals(typeDemandeLibelle)) {
            Map<String, String> erreurs =
                transfertService.validerAncienVisa(reference, dateDebut, dateFin);
            if (!erreurs.isEmpty()) {
                model.addAttribute("erreurs", erreurs);
                model.addAttribute("menuActif", "renouvellement");
                model.addAttribute("etapeActuelle", 5);
                return "transfert/etape5_ancien_visa";
            }
            session.setAttribute("referenceAncienVisa", reference);
            session.setAttribute("dateDebutAncienVisa", dateDebut);
            session.setAttribute("dateFinAncienVisa", dateFin);
            return "redirect:/duplicata/etape6";
        }

        // Duplicata — comportement existant
        Map<String, String> erreurs =
            duplicataService.validerInfosTitre(reference, dateDebut, dateFin);
        if (!erreurs.isEmpty()) {
            model.addAttribute("erreurs", erreurs);
            model.addAttribute("menuActif", "duplicata");
            model.addAttribute("etapeActuelle", 5);
            return "duplicata/etape5";
        }
        session.setAttribute("referenceNouveauTitre", reference);
        session.setAttribute("dateDebutNouveauTitre", dateDebut);
        session.setAttribute("dateFinNouveauTitre", dateFin);
        return "redirect:/duplicata/etape6";
    }

    @GetMapping("/etape6")
    public String etape6(HttpSession session, Model model) {
        String typeDemandeLibelle =
            (String) session.getAttribute("typeDemandeLibelle");

        if ("Transfert de visa".equals(typeDemandeLibelle)) {
            model.addAttribute("numeroPasseport",
                session.getAttribute("nouveauNumeroPasseport"));
            model.addAttribute("dateDelivrance",
                session.getAttribute("nouvelleDateDelivrance"));
            model.addAttribute("dateExpiration",
                session.getAttribute("nouvelleDateExpiration"));
            model.addAttribute("paysDelivrance",
                session.getAttribute("nouveauPaysDelivrance"));
            model.addAttribute("menuActif", "renouvellement");
            model.addAttribute("etapeActuelle", 6);
            return "transfert/etape6_nouveau_passeport";
        }

        // Duplicata — comportement existant
        model.addAttribute("referenceAncienne",
            session.getAttribute("referenceAncienneCarte"));
        model.addAttribute("dateDebutAncienne",
            session.getAttribute("dateDebutAncienneCarte"));
        model.addAttribute("dateFinAncienne",
            session.getAttribute("dateFinAncienneCarte"));
        model.addAttribute("menuActif", "duplicata");
        model.addAttribute("etapeActuelle", 6);
        return "duplicata/etape6";
    }

   @PostMapping("/etape6")
    public String traiterEtape6(
            @RequestParam(required = false) String referenceAncienne,
            @RequestParam(required = false) String numeroPasseport,
            @RequestParam(required = false)
                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                LocalDate dateDebutAncienne,
            @RequestParam(required = false)
                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                LocalDate dateFinAncienne,
            @RequestParam(required = false)
                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                LocalDate dateDelivrance,
            @RequestParam(required = false)
                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                LocalDate dateExpiration,
            @RequestParam(required = false) String paysDelivrance,
            HttpSession session,
            Model model) {

        String typeDemandeLibelle =
            (String) session.getAttribute("typeDemandeLibelle");

        if ("Transfert de visa".equals(typeDemandeLibelle)) {
            Map<String, String> erreurs =
                transfertService.validerNouveauPasseport(
                    numeroPasseport, dateDelivrance,
                    dateExpiration, paysDelivrance);
            if (!erreurs.isEmpty()) {
                model.addAttribute("erreurs", erreurs);
                model.addAttribute("menuActif", "renouvellement");
                model.addAttribute("etapeActuelle", 6);
                return "transfert/etape6_nouveau_passeport";
            }
            session.setAttribute("nouveauNumeroPasseport", numeroPasseport);
            session.setAttribute("nouvelleDateDelivrance", dateDelivrance);
            session.setAttribute("nouvelleDateExpiration", dateExpiration);
            session.setAttribute("nouveauPaysDelivrance", paysDelivrance);
            return "redirect:/duplicata/resume";
        }

        // Duplicata — comportement existant
        Map<String, String> erreurs =
            duplicataService.validerAncienneCarte(
                referenceAncienne, dateDebutAncienne, dateFinAncienne);
        if (!erreurs.isEmpty()) {
            model.addAttribute("erreurs", erreurs);
            model.addAttribute("menuActif", "duplicata");
            model.addAttribute("etapeActuelle", 6);
            return "duplicata/etape6";
        }
        session.setAttribute("referenceAncienneCarte", referenceAncienne);
        session.setAttribute("dateDebutAncienneCarte", dateDebutAncienne);
        session.setAttribute("dateFinAncienneCarte", dateFinAncienne);
        return "redirect:/duplicata/resume";
    }
    
    @GetMapping("/resume")
    public String resume(HttpSession session, Model model) {
        Integer idDemande =
            (Integer) session.getAttribute("idDemandeNouveauTitre");
        Demande demande = duplicataService.findDemandeById(idDemande);
        alimenterModeleResume(session, model, demande);

        String typeDemandeLibelle =
            (String) session.getAttribute("typeDemandeLibelle");

        if ("Transfert de visa".equals(typeDemandeLibelle)) {
            model.addAttribute("menuActif", "renouvellement");
        } else {
            model.addAttribute("menuActif", "duplicata");
        }

        model.addAttribute("typeDemandeLibelle", typeDemandeLibelle);
        model.addAttribute("etapeActuelle", 7);
        return "duplicata/resume";
    }

    @PostMapping("/confirmer")
    public String confirmer(HttpSession session, Model model) {
        String typeDemandeLibelle =
            (String) session.getAttribute("typeDemandeLibelle");
        Integer idDemande =
            (Integer) session.getAttribute("idDemandeNouveauTitre");
        Integer idPasseport =
            (Integer) session.getAttribute("idPasseportSauvegarde");

        // ← Divergence finale
        if ("Transfert de visa".equals(typeDemandeLibelle)) {
            try {
                Visa ancienVisa = transfertService.insererAncienVisa(
                    idDemande,
                    idPasseport,
                    (String) session.getAttribute("referenceAncienVisa"),
                    (LocalDate) session.getAttribute("dateDebutAncienVisa"),
                    (LocalDate) session.getAttribute("dateFinAncienVisa"));

                Demande nouvelleDemande = transfertService.finaliserTransfert(
                    idDemande,
                    ancienVisa,
                    (String) session.getAttribute("nouveauNumeroPasseport"),
                    (LocalDate) session.getAttribute("nouvelleDateDelivrance"),
                    (LocalDate) session.getAttribute("nouvelleDateExpiration"),
                    (String) session.getAttribute("nouveauPaysDelivrance"));

                log.info("Transfert finalise - origine={} nouvelle={}",
                    idDemande, nouvelleDemande.getId());
                session.invalidate();
                return "redirect:/demande/confirmation/"
                    + nouvelleDemande.getId();

            } catch (Exception e) {
                log.error("Erreur confirmation transfert idDemande={}",
                    idDemande, e);
                model.addAttribute("erreur",
                    "Erreur lors de la confirmation : " + e.getMessage());
                return "transfert/etape6_nouveau_passeport";
            }
        }

        // Duplicata — comportement existant inchangé
        Integer typeVisaId = (Integer) session.getAttribute("typeVisaId");
        LocalDate dateDemande = (LocalDate) session.getAttribute("dateDemande");
        try {
            CarteResident nouvelleCartePhase1 = duplicataService.finaliserPhase1(
                idDemande, idPasseport,
                (String) session.getAttribute("referenceNouveauTitre"),
                (LocalDate) session.getAttribute("dateDebutNouveauTitre"),
                (LocalDate) session.getAttribute("dateFinNouveauTitre"));

            duplicataService.enregistrerAncienneCarte(
                idDemande, idPasseport,
                (String) session.getAttribute("referenceAncienneCarte"),
                (LocalDate) session.getAttribute("dateDebutAncienneCarte"),
                (LocalDate) session.getAttribute("dateFinAncienneCarte"));

            Demande demandeDuplicata = duplicataService.finaliserPhase3(
                idDemande, idPasseport, typeVisaId, dateDemande);

            log.info("Duplicata finalise - origine={} duplicata={}",
                idDemande, demandeDuplicata.getId());
            session.invalidate();
            return "redirect:/demande/confirmation/" + demandeDuplicata.getId();

        } catch (Exception e) {
            log.error("Erreur confirmation duplicata idDemande={}", idDemande, e);
            model.addAttribute("erreur",
                "Erreur lors de la confirmation : " + e.getMessage());
            Demande demande = duplicataService.findDemandeById(idDemande);
            alimenterModeleResume(session, model, demande);
            model.addAttribute("menuActif", "duplicata");
            model.addAttribute("etapeActuelle", 7);
            return "duplicata/resume";
        }
    }

    private void alimenterModeleResume(
            HttpSession session, Model model, Demande demande) {

        List<PieceDemande> communes =
            duplicataService.findPiecesCommunesByDemande(demande);
        List<PieceDemandeSpecifique> specifiques =
            duplicataService.findPiecesSpecifiquesByDemande(demande);
        Map<Integer, UploadPiece> uploadsCommunes =
            duplicataService.getDerniersUploadsCommunes(communes);
        Map<Integer, UploadPiece> uploadsSpecifiques =
            duplicataService.getDerniersUploadsSpecifiques(specifiques);

        model.addAttribute("demande", demande);
        model.addAttribute("piecesCommunes", communes);
        model.addAttribute("piecesSpecifiques", specifiques);
        model.addAttribute("uploadsCommunes", uploadsCommunes);
        model.addAttribute("uploadsSpecifiques", uploadsSpecifiques);

        // Données duplicata
        model.addAttribute("referenceNouveauTitre",
            session.getAttribute("referenceNouveauTitre"));
        model.addAttribute("dateDebutNouveauTitre",
            session.getAttribute("dateDebutNouveauTitre"));
        model.addAttribute("dateFinNouveauTitre",
            session.getAttribute("dateFinNouveauTitre"));
        model.addAttribute("referenceAncienneCarte",
            session.getAttribute("referenceAncienneCarte"));
        model.addAttribute("dateDebutAncienneCarte",
            session.getAttribute("dateDebutAncienneCarte"));
        model.addAttribute("dateFinAncienneCarte",
            session.getAttribute("dateFinAncienneCarte"));

        // Données transfert
        model.addAttribute("referenceAncienVisa",
            session.getAttribute("referenceAncienVisa"));
        model.addAttribute("dateDebutAncienVisa",
            session.getAttribute("dateDebutAncienVisa"));
        model.addAttribute("dateFinAncienVisa",
            session.getAttribute("dateFinAncienVisa"));
        model.addAttribute("nouveauNumeroPasseport",
            session.getAttribute("nouveauNumeroPasseport"));
        model.addAttribute("nouvelleDateDelivrance",
            session.getAttribute("nouvelleDateDelivrance"));
        model.addAttribute("nouvelleDateExpiration",
            session.getAttribute("nouvelleDateExpiration"));
        model.addAttribute("nouveauPaysDelivrance",
            session.getAttribute("nouveauPaysDelivrance"));
    }
}
