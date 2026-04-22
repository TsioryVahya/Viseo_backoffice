package com.viseo.backoffice.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.viseo.backoffice.model.TypePieceCommune;
import com.viseo.backoffice.model.TypePieceSpecifique;
import com.viseo.backoffice.model.UploadPiece;
import com.viseo.backoffice.service.TitreService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/demande/autre")
public class TitreController {

    private static final Logger log = LoggerFactory.getLogger(TitreController.class);

    private final TitreService titreService;

    public TitreController(TitreService titreService) {
        this.titreService = titreService;
    }

    @GetMapping("/recherche")
    public String recherche(HttpServletRequest request, Model model) {
        model.addAttribute("typeDemande", request.getParameter("type") != null ? request.getParameter("type") : "duplicata");
        model.addAttribute("etapeActuelle", 1);
        model.addAttribute("menuActif", "nouvelle-demande");
        return "titre/recherche";
    }

    @PostMapping("/recherche")
    public String traiterRecherche(
            @RequestParam String type,
            @RequestParam String nom,
            @RequestParam String prenom,
            @RequestParam String numeroPasseport,
            HttpSession session,
            Model model) {
        Map<String, String> erreurs = titreService.validerRecherche(nom, prenom, numeroPasseport);
        if (!erreurs.isEmpty()) {
            model.addAttribute("erreurs", erreurs);
            model.addAttribute("typeDemande", type);
            model.addAttribute("etapeActuelle", 1);
            model.addAttribute("menuActif", "nouvelle-demande");
            return "titre/recherche";
        }

        Map<String, Object> resultat = titreService.traiterRecherche(nom, prenom, numeroPasseport, type);
        if (resultat.get("erreurMetier") != null) {
            model.addAttribute("erreurRecherche", resultat.get("erreurMetier"));
            model.addAttribute("typeDemande", type);
            model.addAttribute("etapeActuelle", 1);
            model.addAttribute("menuActif", "nouvelle-demande");
            return "titre/recherche";
        }

        session.setAttribute("typeDemande", type);
        session.setAttribute("typeDemandeId", "duplicata".equals(type) ? 2 : 3);

        if (Boolean.TRUE.equals(resultat.get("demandeurTrouve"))) {
            Demandeur d = (Demandeur) resultat.get("demandeur");
            Passeport p = (Passeport) resultat.get("passeport");
            session.setAttribute("demandeurTrouve", true);
            session.setAttribute("idDemandeurExistant", d.getId());
            session.setAttribute("idPasseportExistant", p.getId());

            if ("duplicata".equals(type)) {
                CarteResident carte = (CarteResident) resultat.get("carteResident");
                session.setAttribute("idCarteResidentExistante", carte.getId());
                session.setAttribute("typeVisaId", carte.getDemande().getTypeVisa().getId());
                return "redirect:/demande/autre/titre";
            }

            if (resultat.get("typeVisaId") != null) {
                session.setAttribute("typeVisaId", resultat.get("typeVisaId"));
            }
            return "redirect:/demande/autre/nouveau-passeport";
        }

        session.setAttribute("demandeurTrouve", false);
        return "redirect:/demande/autre/etape1";
    }

    @GetMapping("/nouveau-passeport")
    public String nouveauPasseport(HttpSession session, Model model) {
        model.addAttribute("typeDemande", session.getAttribute("typeDemande"));
        model.addAttribute("menuActif", "nouvelle-demande");
        return "titre/nouveau_passeport";
    }

    @PostMapping("/nouveau-passeport")
    public String traiterNouveauPasseport(
            @RequestParam String numeroPasseport,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDelivrance,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateExpiration,
            @RequestParam String paysDelivrance,
            HttpSession session,
            Model model) {
        Map<String, String> erreurs = titreService.validerPasseport(
                numeroPasseport,
                dateDelivrance,
                dateExpiration,
                paysDelivrance);
        if (!erreurs.isEmpty()) {
            model.addAttribute("erreurs", erreurs);
            model.addAttribute("typeDemande", session.getAttribute("typeDemande"));
            model.addAttribute("menuActif", "nouvelle-demande");
            return "titre/nouveau_passeport";
        }

        session.setAttribute("nouveauNumeroPasseport", numeroPasseport);
        session.setAttribute("nouvelleDateDelivrance", dateDelivrance);
        session.setAttribute("nouvelleDateExpiration", dateExpiration);
        session.setAttribute("nouveauPaysDelivrance", paysDelivrance);
        return "redirect:/demande/autre/titre";
    }

    @GetMapping("/titre")
    public String titre(HttpSession session, Model model) {
        model.addAttribute("typeDemande", session.getAttribute("typeDemande"));
        model.addAttribute("menuActif", "nouvelle-demande");
        return "titre/titre";
    }

    @PostMapping("/titre")
    public String finaliserTrouve(
            @RequestParam String reference,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
            HttpSession session,
            Model model) {
        Map<String, String> erreurs = titreService.validerTitre(reference, dateDebut, dateFin);
        if (!erreurs.isEmpty()) {
            model.addAttribute("erreurs", erreurs);
            model.addAttribute("typeDemande", session.getAttribute("typeDemande"));
            model.addAttribute("menuActif", "nouvelle-demande");
            return "titre/titre";
        }

        String typeDemande = (String) session.getAttribute("typeDemande");
        try {
            Demande demande;
            if ("duplicata".equals(typeDemande)) {
                Demandeur demandeur = titreService.findDemandeurById((Integer) session.getAttribute("idDemandeurExistant"));
                Passeport passeport = titreService.findPasseportById((Integer) session.getAttribute("idPasseportExistant"));
                CarteResident carte = titreService.findCarteResidentById((Integer) session.getAttribute("idCarteResidentExistante"));
                demande = titreService.finaliserDuplicataTrouve(demandeur, passeport, carte, reference, dateDebut, dateFin);
            } else {
                Demandeur demandeur = titreService.findDemandeurById((Integer) session.getAttribute("idDemandeurExistant"));
                Passeport nouveauPasseport = titreService.construireNouveauPasseport(
                        demandeur,
                        (String) session.getAttribute("nouveauNumeroPasseport"),
                        (LocalDate) session.getAttribute("nouvelleDateDelivrance"),
                        (LocalDate) session.getAttribute("nouvelleDateExpiration"),
                        (String) session.getAttribute("nouveauPaysDelivrance"));
                Integer typeVisaId = (Integer) session.getAttribute("typeVisaId");
                demande = titreService.finaliserTransfertTrouve(
                        demandeur,
                        nouveauPasseport,
                        typeVisaId,
                        reference,
                        dateDebut,
                        dateFin);
            }
            log.info("Titre delivre - demande id={} type={}", demande.getId(), typeDemande);
            session.invalidate();
            return "redirect:/demande/confirmation/" + demande.getId();
        } catch (Exception e) {
            log.error("Erreur finalisation titre type={}", typeDemande, e);
            model.addAttribute("erreur", "Erreur : " + e.getMessage());
            model.addAttribute("typeDemande", typeDemande);
            model.addAttribute("menuActif", "nouvelle-demande");
            return "titre/titre";
        }
    }

    @GetMapping("/etape1")
    public String etape1(HttpSession session, Model model) {
        model.addAttribute("nationalites", titreService.findAllNationalites());
        model.addAttribute("situationsFamiliales", titreService.findAllSituationsFamiliales());
        model.addAttribute("typeDemande", session.getAttribute("typeDemande"));
        model.addAttribute("demandeur", session.getAttribute("demandeur"));
        model.addAttribute("etapeActuelle", 2);
        model.addAttribute("menuActif", "nouvelle-demande");
        return "titre/etape1";
    }

    @PostMapping("/etape1")
    public String traiterEtape1(
            @RequestParam String nom,
            @RequestParam String prenom,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateNaissance,
            @RequestParam String lieuNaissance,
            @RequestParam String telephone,
            @RequestParam String email,
            @RequestParam String adresse,
            @RequestParam Integer idNationalite,
            @RequestParam Integer idSituationFamiliale,
            HttpSession session,
            Model model) {
        Map<String, String> erreurs = titreService.validerDemandeur(
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
            model.addAttribute("nationalites", titreService.findAllNationalites());
            model.addAttribute("situationsFamiliales", titreService.findAllSituationsFamiliales());
            model.addAttribute("erreurs", erreurs);
            model.addAttribute("typeDemande", session.getAttribute("typeDemande"));
            model.addAttribute("etapeActuelle", 2);
            model.addAttribute("menuActif", "nouvelle-demande");
            return "titre/etape1";
        }

        Demandeur demandeur = titreService.construireDemandeur(
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
        return "redirect:/demande/autre/etape2";
    }

    @GetMapping("/etape2")
    public String etape2(HttpSession session, Model model) {
        model.addAttribute("typeDemande", session.getAttribute("typeDemande"));
        model.addAttribute("passeport", session.getAttribute("passeport"));
        model.addAttribute("numeroReferenceVisa", session.getAttribute("numeroReferenceVisa"));
        model.addAttribute("dateExpirationVisa", session.getAttribute("dateExpirationVisa"));
        model.addAttribute("etapeActuelle", 3);
        model.addAttribute("menuActif", "nouvelle-demande");
        return "titre/etape2";
    }

    @PostMapping("/etape2")
    public String traiterEtape2(
            @RequestParam String numeroPasseport,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDelivrance,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateExpiration,
            @RequestParam String paysDelivrance,
            @RequestParam String numeroReferenceVisa,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateExpirationVisa,
            HttpSession session,
            Model model) {
        Map<String, String> erreurs = titreService.validerEtape2(
                numeroPasseport,
                dateDelivrance,
                dateExpiration,
                paysDelivrance,
                numeroReferenceVisa,
                dateExpirationVisa);
        if (!erreurs.isEmpty()) {
            model.addAttribute("erreurs", erreurs);
            model.addAttribute("typeDemande", session.getAttribute("typeDemande"));
            model.addAttribute("etapeActuelle", 3);
            model.addAttribute("menuActif", "nouvelle-demande");
            return "titre/etape2";
        }

        session.setAttribute("numeroReferenceVisa", numeroReferenceVisa);
        session.setAttribute("dateExpirationVisa", dateExpirationVisa);
        Map<String, Object> passeportData = new HashMap<>();
        passeportData.put("numero", numeroPasseport);
        passeportData.put("dateDelivrance", dateDelivrance);
        passeportData.put("dateExpiration", dateExpiration);
        passeportData.put("paysDelivrance", paysDelivrance);
        session.setAttribute("passeportData", passeportData);
        return "redirect:/demande/autre/etape3a";
    }

    @GetMapping("/etape3a")
    public String etape3a(HttpSession session, Model model) {
        Integer typeVisaId = (Integer) session.getAttribute("typeVisaId");
        String typeDemande = (String) session.getAttribute("typeDemande");
        model.addAttribute("typesVisa", titreService.findAllTypesVisa());
        model.addAttribute("piecesCommunes", titreService.findAllPiecesCommunes());
        model.addAttribute("typeDemande", typeDemande);
        model.addAttribute("typeVisaId", typeVisaId);
        if ("duplicata".equals(typeDemande) && typeVisaId != null) {
            model.addAttribute("typeVisaLibelle", titreService.findTypeVisaLibelleById(typeVisaId));
        }
        model.addAttribute("dateDemande", session.getAttribute("dateDemande"));
        model.addAttribute("piecesChecked", session.getAttribute("piecesCommunes"));
        model.addAttribute("etapeActuelle", 4);
        model.addAttribute("menuActif", "nouvelle-demande");
        return "titre/etape3a";
    }

    @PostMapping("/etape3a")
    public String traiterEtape3a(
            @RequestParam(required = false) Integer typeVisaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDemande,
            @RequestParam Map<String, String> params,
            HttpSession session,
            Model model) {
        String typeDemande = (String) session.getAttribute("typeDemande");
        LocalDate dateExpirationVisa = (LocalDate) session.getAttribute("dateExpirationVisa");

        Map<Integer, Boolean> piecesCommunes = titreService.extractPiecesFromParams(params, "pieceCommune_");
        Map<String, String> erreurs = titreService.validerEtape3a(
                typeDemande,
                typeVisaId,
                dateDemande,
                dateExpirationVisa,
                piecesCommunes,
                titreService.findAllPiecesCommunes());
        if (!erreurs.isEmpty()) {
            model.addAttribute("erreurs", erreurs);
            model.addAttribute("typesVisa", titreService.findAllTypesVisa());
            model.addAttribute("piecesCommunes", titreService.findAllPiecesCommunes());
            model.addAttribute("typeDemande", typeDemande);
            model.addAttribute("typeVisaId", typeVisaId);
            if ("duplicata".equals(typeDemande) && typeVisaId != null) {
                model.addAttribute("typeVisaLibelle", titreService.findTypeVisaLibelleById(typeVisaId));
            }
            model.addAttribute("piecesChecked", piecesCommunes);
            model.addAttribute("dateDemande", dateDemande);
            model.addAttribute("etapeActuelle", 4);
            model.addAttribute("menuActif", "nouvelle-demande");
            return "titre/etape3a";
        }

        session.setAttribute("typeVisaId", typeVisaId);
        session.setAttribute("dateDemande", dateDemande);
        session.setAttribute("piecesCommunes", piecesCommunes);
        return "redirect:/demande/autre/etape3b";
    }

    @GetMapping("/etape3b")
    public String etape3b(HttpSession session, Model model) {
        Integer typeVisaId = (Integer) session.getAttribute("typeVisaId");
        model.addAttribute("piecesSpecifiques", titreService.findPiecesSpecifiquesByTypeVisa(typeVisaId));
        model.addAttribute("typeVisaId", typeVisaId);
        model.addAttribute("typeVisaLibelle", titreService.findTypeVisaLibelleById(typeVisaId));
        model.addAttribute("piecesChecked", session.getAttribute("piecesSpecifiques"));
        model.addAttribute("typeDemande", session.getAttribute("typeDemande"));
        model.addAttribute("etapeActuelle", 4);
        model.addAttribute("menuActif", "nouvelle-demande");
        return "titre/etape3b";
    }

    @PostMapping("/etape3b")
    public String traiterEtape3b(
            @RequestParam Map<String, String> params,
            HttpSession session,
            Model model) {
        Integer typeVisaId = (Integer) session.getAttribute("typeVisaId");
        Map<Integer, Boolean> piecesSpecifiques = titreService.extractPiecesFromParams(params, "pieceSpecifique_");

        List<TypePieceSpecifique> piecesRef = titreService.findPiecesSpecifiquesByTypeVisa(typeVisaId);
        Map<String, String> erreurs = titreService.validerPiecesSpecifiques(piecesSpecifiques, piecesRef);
        if (!erreurs.isEmpty()) {
            log.warn("Pieces specifiques obligatoires manquantes - typeVisa={}", typeVisaId);
            model.addAttribute("erreurs", erreurs);
            model.addAttribute("piecesSpecifiques", piecesRef);
            model.addAttribute("piecesChecked", piecesSpecifiques);
            model.addAttribute("typeVisaId", typeVisaId);
            model.addAttribute("typeVisaLibelle", titreService.findTypeVisaLibelleById(typeVisaId));
            model.addAttribute("typeDemande", session.getAttribute("typeDemande"));
            model.addAttribute("etapeActuelle", 4);
            model.addAttribute("menuActif", "nouvelle-demande");
            return "titre/etape3b";
        }

        session.setAttribute("piecesSpecifiques", piecesSpecifiques);

        Demandeur demandeur = (Demandeur) session.getAttribute("demandeur");
        @SuppressWarnings("unchecked")
        Map<String, Object> passeportData = (Map<String, Object>) session.getAttribute("passeportData");
        Passeport passeport = titreService.construireNouveauPasseport(
                demandeur,
                (String) passeportData.get("numero"),
                (LocalDate) passeportData.get("dateDelivrance"),
                (LocalDate) passeportData.get("dateExpiration"),
                (String) passeportData.get("paysDelivrance"));

        @SuppressWarnings("unchecked")
        Map<Integer, Boolean> piecesCommunes = (Map<Integer, Boolean>) session.getAttribute("piecesCommunes");
        Demande demande = titreService.creerDemandeEnCoursDeSaisie(
                demandeur,
                passeport,
                (Integer) session.getAttribute("typeVisaId"),
                (Integer) session.getAttribute("typeDemandeId"),
                (LocalDate) session.getAttribute("dateDemande"),
                (String) session.getAttribute("numeroReferenceVisa"),
                (LocalDate) session.getAttribute("dateExpirationVisa"),
                piecesCommunes,
                piecesSpecifiques);

        session.setAttribute("idDemandeEnCours", demande.getId());
        session.setAttribute("idPasseportEnCours", titreService.findPasseportByDemande(demande).getId());
        return "redirect:/demande/autre/etape4";
    }

    @GetMapping("/etape4")
    public String etape4(HttpSession session, Model model) {
        Integer idDemande = (Integer) session.getAttribute("idDemandeEnCours");
        Demande demande = titreService.findDemandeById(idDemande);
        List<PieceDemande> communes = titreService.findPiecesCommunesByDemande(demande);
        List<PieceDemandeSpecifique> specifiques = titreService.findPiecesSpecifiquesByDemande(demande);
        Map<Integer, UploadPiece> uploadsCommunes = titreService.getDerniersUploadsCommunes(communes);
        Map<Integer, UploadPiece> uploadsSpecifiques = titreService.getDerniersUploadsSpecifiques(specifiques);
        boolean peutContinuer = titreService.peutContinuerApresUpload(demande);

        model.addAttribute("demande", demande);
        model.addAttribute("piecesCommunes", communes);
        model.addAttribute("piecesSpecifiques", specifiques);
        model.addAttribute("derniersUploadsCommunes", uploadsCommunes);
        model.addAttribute("derniersUploadsSpecifiques", uploadsSpecifiques);
        model.addAttribute("peutContinuer", peutContinuer);
        model.addAttribute("today", LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
        model.addAttribute("etapeActuelle", 5);
        model.addAttribute("menuActif", "nouvelle-demande");
        return "titre/etape4_upload";
    }

    @PostMapping("/etape4/piece")
    public String uploadEtape4(
            @RequestParam Integer idPiece,
            @RequestParam String typePiece,
            @RequestParam MultipartFile fichier,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateUpload,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        Integer idDemande = (Integer) session.getAttribute("idDemandeEnCours");
        try {
            titreService.traiterUpload(idDemande, idPiece, typePiece, fichier, dateUpload);
            redirectAttributes.addFlashAttribute("succes", "Fichier uploade avec succes.");
        } catch (Exception e) {
            log.error("Erreur upload wizard - piece={} demande={}", idPiece, idDemande, e);
            redirectAttributes.addFlashAttribute("erreur", "Erreur upload : " + e.getMessage());
        }
        return "redirect:/demande/autre/etape4";
    }

    @GetMapping("/etape5")
    public String etape5(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Integer idDemande = (Integer) session.getAttribute("idDemandeEnCours");
        Demande demande = titreService.findDemandeById(idDemande);

        List<String> manquantes = titreService.getPiecesObligatoiresNonUploadees(demande);
        if (!manquantes.isEmpty()) {
            log.warn("Acces etape5 bloque - pieces manquantes : {}", manquantes);
            redirectAttributes.addFlashAttribute("erreur", "Uploadez toutes les pieces obligatoires avant de continuer.");
            return "redirect:/demande/autre/etape4";
        }

        model.addAttribute("typeDemande", session.getAttribute("typeDemande"));
        model.addAttribute("reference", session.getAttribute("referencetitre"));
        model.addAttribute("dateDebut", session.getAttribute("titredateDebut"));
        model.addAttribute("dateFin", session.getAttribute("titredateFin"));
        model.addAttribute("etapeActuelle", 6);
        model.addAttribute("menuActif", "nouvelle-demande");
        return "titre/etape5";
    }

    @PostMapping("/etape5")
    public String traiterEtape5(
            @RequestParam String reference,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
            HttpSession session,
            Model model) {
        Map<String, String> erreurs = titreService.validerTitre(reference, dateDebut, dateFin);
        if (!erreurs.isEmpty()) {
            model.addAttribute("erreurs", erreurs);
            model.addAttribute("typeDemande", session.getAttribute("typeDemande"));
            model.addAttribute("etapeActuelle", 6);
            model.addAttribute("menuActif", "nouvelle-demande");
            return "titre/etape5";
        }
        session.setAttribute("referencetitre", reference);
        session.setAttribute("titredateDebut", dateDebut);
        session.setAttribute("titredateFin", dateFin);
        return "redirect:/demande/autre/etape6";
    }

    @GetMapping("/etape6")
    public String etape6(HttpSession session, Model model) {
        Integer idDemande = (Integer) session.getAttribute("idDemandeEnCours");
        Demande demande = titreService.findDemandeById(idDemande);
        List<PieceDemande> communes = titreService.findPiecesCommunesByDemande(demande);
        List<PieceDemandeSpecifique> specifiques = titreService.findPiecesSpecifiquesByDemande(demande);
        Map<Integer, UploadPiece> uploadsCommunes = titreService.getDerniersUploadsCommunes(communes);
        Map<Integer, UploadPiece> uploadsSpecifiques = titreService.getDerniersUploadsSpecifiques(specifiques);

        model.addAttribute("demande", demande);
        model.addAttribute("piecesCommunes", communes);
        model.addAttribute("piecesSpecifiques", specifiques);
        model.addAttribute("uploadsCommunes", uploadsCommunes);
        model.addAttribute("uploadsSpecifiques", uploadsSpecifiques);
        model.addAttribute("reference", session.getAttribute("referencetitre"));
        model.addAttribute("dateDebut", session.getAttribute("titredateDebut"));
        model.addAttribute("dateFin", session.getAttribute("titredateFin"));
        model.addAttribute("typeDemande", session.getAttribute("typeDemande"));
        model.addAttribute("etapeActuelle", 7);
        model.addAttribute("menuActif", "nouvelle-demande");
        return "titre/etape6";
    }

    @PostMapping("/etape6")
    public String confirmerEtape6(HttpSession session, Model model) {
        Integer idDemande = (Integer) session.getAttribute("idDemandeEnCours");
        Integer idPasseport = (Integer) session.getAttribute("idPasseportEnCours");
        String typeDemande = (String) session.getAttribute("typeDemande");
        String reference = (String) session.getAttribute("referencetitre");
        LocalDate dateDebut = (LocalDate) session.getAttribute("titredateDebut");
        LocalDate dateFin = (LocalDate) session.getAttribute("titredateFin");

        try {
            Demande demande = titreService.finaliserDemandeNonTrouve(
                    idDemande,
                    typeDemande,
                    idPasseport,
                    reference,
                    dateDebut,
                    dateFin);
            log.info("Demande finalisee id={} type={} statut=Titre delivre", demande.getId(), typeDemande);
            session.invalidate();
            return "redirect:/demande/confirmation/" + demande.getId();
        } catch (Exception e) {
            log.error("Erreur finalisation demande id={}", idDemande, e);
            model.addAttribute("erreur", "Erreur lors de la confirmation : " + e.getMessage());
            model.addAttribute("menuActif", "nouvelle-demande");
            return "titre/etape6";
        }
    }
}
