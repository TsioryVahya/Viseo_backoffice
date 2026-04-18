package com.viseo.backoffice.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.viseo.backoffice.model.Demande;
import com.viseo.backoffice.model.Demandeur;
import com.viseo.backoffice.model.Nationalite;
import com.viseo.backoffice.model.Passeport;
import com.viseo.backoffice.model.PieceDemande;
import com.viseo.backoffice.model.PieceDemandeSpecifique;
import com.viseo.backoffice.model.SituationFamiliale;
import com.viseo.backoffice.model.StatutDemande;
import com.viseo.backoffice.model.StatutDemandeType;
import com.viseo.backoffice.model.TypeDemande;
import com.viseo.backoffice.model.TypePieceCommune;
import com.viseo.backoffice.model.TypePieceSpecifique;
import com.viseo.backoffice.model.TypeVisa;
import com.viseo.backoffice.model.VisaTransformable;
import com.viseo.backoffice.service.DemandeService;
import com.viseo.backoffice.service.DemandeurService;
import com.viseo.backoffice.service.NationaliteService;
import com.viseo.backoffice.service.PasseportService;
import com.viseo.backoffice.service.PieceDemandeService;
import com.viseo.backoffice.service.PieceDemandeSpecifiqueService;
import com.viseo.backoffice.service.SituationFamilialeService;
import com.viseo.backoffice.service.StatutDemandeService;
import com.viseo.backoffice.service.StatutDemandeTypeService;
import com.viseo.backoffice.service.TypeDemandeService;
import com.viseo.backoffice.service.TypePieceCommuneService;
import com.viseo.backoffice.service.TypePieceSpecifiqueService;
import com.viseo.backoffice.service.TypeVisaService;
import com.viseo.backoffice.service.VisaTransformableService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/demande")
public class DemandeNouveauTitreController {

    private static final String SESSION_DEMANDEUR = "demandeur";
    private static final String SESSION_PASSEPORT = "passeport";
    private static final String SESSION_TYPE_VISA_ID = "typeVisaId";
    private static final String SESSION_PIECES_COMMUNES = "piecesCommunes";
    private static final String SESSION_PIECES_SPECIFIQUES = "piecesSpecifiques";
    private static final String SESSION_NUMERO_REFERENCE_VISA = "numeroReferenceVisa";
    private static final String SESSION_DATE_EXPIRATION_VISA = "dateExpirationVisa";
    private static final String SESSION_DATE_DEMANDE = "dateDemande";
    private static final String SESSION_AVERTISSEMENT_VISA = "avertissementVisa";

    private final NationaliteService nationaliteService;
    private final SituationFamilialeService situationFamilialeService;
    private final TypeVisaService typeVisaService;
    private final TypePieceCommuneService typePieceCommuneService;
    private final TypePieceSpecifiqueService typePieceSpecifiqueService;
    private final DemandeurService demandeurService;
    private final PasseportService passeportService;
    private final DemandeService demandeService;
    private final StatutDemandeService statutDemandeService;
    private final PieceDemandeService pieceDemandeService;
    private final PieceDemandeSpecifiqueService pieceDemandeSpecifiqueService;
    private final VisaTransformableService visaTransformableService;
    private final TypeDemandeService typeDemandeService;
    private final StatutDemandeTypeService statutDemandeTypeService;

    public DemandeNouveauTitreController(
            NationaliteService nationaliteService,
            SituationFamilialeService situationFamilialeService,
            TypeVisaService typeVisaService,
            TypePieceCommuneService typePieceCommuneService,
            TypePieceSpecifiqueService typePieceSpecifiqueService,
            DemandeurService demandeurService,
            PasseportService passeportService,
            DemandeService demandeService,
            StatutDemandeService statutDemandeService,
            PieceDemandeService pieceDemandeService,
            PieceDemandeSpecifiqueService pieceDemandeSpecifiqueService,
            VisaTransformableService visaTransformableService,
            TypeDemandeService typeDemandeService,
            StatutDemandeTypeService statutDemandeTypeService) {
        this.nationaliteService = nationaliteService;
        this.situationFamilialeService = situationFamilialeService;
        this.typeVisaService = typeVisaService;
        this.typePieceCommuneService = typePieceCommuneService;
        this.typePieceSpecifiqueService = typePieceSpecifiqueService;
        this.demandeurService = demandeurService;
        this.passeportService = passeportService;
        this.demandeService = demandeService;
        this.statutDemandeService = statutDemandeService;
        this.pieceDemandeService = pieceDemandeService;
        this.pieceDemandeSpecifiqueService = pieceDemandeSpecifiqueService;
        this.visaTransformableService = visaTransformableService;
        this.typeDemandeService = typeDemandeService;
        this.statutDemandeTypeService = statutDemandeTypeService;
    }

    @GetMapping("/nouveau/etape1")
    public String afficherEtape1(Model model, HttpSession session) {
        initialiserModeleEtape1(model);

        Demandeur demandeur = (Demandeur) session.getAttribute(SESSION_DEMANDEUR);
        model.addAttribute("form", formEtape1DepuisSession(demandeur));
        model.addAttribute("erreurs", Collections.emptyMap());
        return "demande/etape1";
    }

    @PostMapping("/nouveau/etape1")
    public String traiterEtape1(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String prenom,
            @RequestParam(required = false) String dateNaissance,
            @RequestParam(required = false) String lieuNaissance,
            @RequestParam(required = false) String telephone,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String adresse,
            @RequestParam(required = false) String idNationalite,
            @RequestParam(required = false) String idSituationFamiliale,
            Model model,
            HttpSession session) {

        Map<String, String> erreurs = new HashMap<>();
        Map<String, String> form = new HashMap<>();
        form.put("nom", valeur(nom));
        form.put("prenom", valeur(prenom));
        form.put("dateNaissance", valeur(dateNaissance));
        form.put("lieuNaissance", valeur(lieuNaissance));
        form.put("telephone", valeur(telephone));
        form.put("email", valeur(email));
        form.put("adresse", valeur(adresse));
        form.put("idNationalite", valeur(idNationalite));
        form.put("idSituationFamiliale", valeur(idSituationFamiliale));

        if (form.get("nom").isEmpty()) {
            erreurs.put("nom", "Le nom est obligatoire.");
        }
        if (form.get("prenom").isEmpty()) {
            erreurs.put("prenom", "Le prenom est obligatoire.");
        }
        if (form.get("lieuNaissance").isEmpty()) {
            erreurs.put("lieuNaissance", "Le lieu de naissance est obligatoire.");
        }
        if (form.get("telephone").isEmpty()) {
            erreurs.put("telephone", "Le telephone est obligatoire.");
        }
        if (form.get("email").isEmpty()) {
            erreurs.put("email", "L'email est obligatoire.");
        }
        if (form.get("adresse").isEmpty()) {
            erreurs.put("adresse", "L'adresse est obligatoire.");
        }

        LocalDate dateNaissanceParsee = null;
        if (form.get("dateNaissance").isEmpty()) {
            erreurs.put("dateNaissance", "La date de naissance est obligatoire.");
        } else {
            try {
                dateNaissanceParsee = LocalDate.parse(form.get("dateNaissance"));
            } catch (DateTimeParseException exception) {
                erreurs.put("dateNaissance", "La date de naissance est invalide.");
            }
        }

        Integer nationaliteId = toInteger(form.get("idNationalite"), "idNationalite", "La nationalite est obligatoire.", erreurs);
        Integer situationId = toInteger(form.get("idSituationFamiliale"), "idSituationFamiliale", "La situation familiale est obligatoire.", erreurs);

        Optional<Nationalite> nationalite = nationaliteId == null ? Optional.empty() : nationaliteService.findById(nationaliteId);
        if (nationaliteId != null && nationalite.isEmpty()) {
            erreurs.put("idNationalite", "La nationalite selectionnee est introuvable.");
        }

        Optional<SituationFamiliale> situationFamiliale = situationId == null ? Optional.empty() : situationFamilialeService.findById(situationId);
        if (situationId != null && situationFamiliale.isEmpty()) {
            erreurs.put("idSituationFamiliale", "La situation familiale selectionnee est introuvable.");
        }

        if (!erreurs.isEmpty()) {
            initialiserModeleEtape1(model);
            model.addAttribute("form", form);
            model.addAttribute("erreurs", erreurs);
            return "demande/etape1";
        }

        Demandeur demandeur = new Demandeur();
        demandeur.setNom(form.get("nom"));
        demandeur.setPrenom(form.get("prenom"));
        demandeur.setDateNaissance(dateNaissanceParsee);
        demandeur.setLieuNaissance(form.get("lieuNaissance"));
        demandeur.setTelephone(form.get("telephone"));
        demandeur.setEmail(form.get("email"));
        demandeur.setAdresse(form.get("adresse"));
        demandeur.setNationalite(nationalite.get());
        demandeur.setSituationFamiliale(situationFamiliale.get());

        session.setAttribute(SESSION_DEMANDEUR, demandeur);
        return "redirect:/demande/nouveau/etape2";
    }

    @GetMapping("/nouveau/etape2")
    public String afficherEtape2(Model model, HttpSession session) {
        if (session.getAttribute(SESSION_DEMANDEUR) == null) {
            return "redirect:/demande/nouveau/etape1";
        }

        Passeport passeport = (Passeport) session.getAttribute(SESSION_PASSEPORT);
        Map<String, String> form = formEtape2DepuisSession(passeport);
        Object numeroReferenceVisa = session.getAttribute(SESSION_NUMERO_REFERENCE_VISA);
        Object dateExpirationVisa = session.getAttribute(SESSION_DATE_EXPIRATION_VISA);
        form.put("numeroReferenceVisa", numeroReferenceVisa == null ? "" : String.valueOf(numeroReferenceVisa));
        form.put("dateExpirationVisa", dateExpirationVisa instanceof LocalDate ? ((LocalDate) dateExpirationVisa).toString() : "");

        model.addAttribute("form", form);
        model.addAttribute("erreurs", Collections.emptyMap());
        return "demande/etape2";
    }

    @PostMapping("/nouveau/etape2")
    public String traiterEtape2(
            @RequestParam(required = false) String numeroPasseport,
            @RequestParam(required = false) String dateDelivrance,
            @RequestParam(required = false) String dateExpiration,
            @RequestParam(required = false) String paysDelivrance,
            @RequestParam(required = false) String numeroReferenceVisa,
            @RequestParam(required = false) String dateExpirationVisa,
            Model model,
            HttpSession session) {

        if (session.getAttribute(SESSION_DEMANDEUR) == null) {
            return "redirect:/demande/nouveau/etape1";
        }

        Map<String, String> erreurs = new HashMap<>();
        Map<String, String> form = new HashMap<>();
        form.put("numeroPasseport", valeur(numeroPasseport));
        form.put("dateDelivrance", valeur(dateDelivrance));
        form.put("dateExpiration", valeur(dateExpiration));
        form.put("paysDelivrance", valeur(paysDelivrance));
        form.put("numeroReferenceVisa", valeur(numeroReferenceVisa));
        form.put("dateExpirationVisa", valeur(dateExpirationVisa));

        if (form.get("numeroPasseport").isEmpty()) {
            erreurs.put("numeroPasseport", "Le numero de passeport est obligatoire.");
        }
        if (form.get("numeroReferenceVisa").isEmpty()) {
            erreurs.put("numeroReferenceVisa", "Le numero de reference du visa transformable est obligatoire.");
        }

        LocalDate dateDelivranceParsee = null;
        LocalDate dateExpirationParsee = null;
        LocalDate dateExpirationVisaParsee = null;

        if (form.get("dateDelivrance").isEmpty()) {
            erreurs.put("dateDelivrance", "La date de delivrance est obligatoire.");
        } else {
            try {
                dateDelivranceParsee = LocalDate.parse(form.get("dateDelivrance"));
            } catch (DateTimeParseException exception) {
                erreurs.put("dateDelivrance", "La date de delivrance est invalide.");
            }
        }

        if (form.get("dateExpiration").isEmpty()) {
            erreurs.put("dateExpiration", "La date d'expiration est obligatoire.");
        } else {
            try {
                dateExpirationParsee = LocalDate.parse(form.get("dateExpiration"));
            } catch (DateTimeParseException exception) {
                erreurs.put("dateExpiration", "La date d'expiration est invalide.");
            }
        }

        if (dateDelivranceParsee != null && dateExpirationParsee != null
                && !dateExpirationParsee.isAfter(dateDelivranceParsee)) {
            erreurs.put("dateExpiration", "La date d'expiration doit etre apres la date de delivrance.");
        }

        if (form.get("dateExpirationVisa").isEmpty()) {
            erreurs.put("dateExpirationVisa", "La date d'expiration du visa transformable est obligatoire.");
        } else {
            try {
                dateExpirationVisaParsee = LocalDate.parse(form.get("dateExpirationVisa"));
            } catch (DateTimeParseException exception) {
                erreurs.put("dateExpirationVisa", "La date d'expiration du visa transformable est invalide.");
            }
        }

        if (!erreurs.isEmpty()) {
            model.addAttribute("form", form);
            model.addAttribute("erreurs", erreurs);
            return "demande/etape2";
        }

        Passeport passeport = new Passeport();
        passeport.setNumeroPasseport(form.get("numeroPasseport"));
        passeport.setDateDelivrance(dateDelivranceParsee);
        passeport.setDateExpiration(dateExpirationParsee);
        passeport.setPaysDelivrance(form.get("paysDelivrance"));

        session.setAttribute(SESSION_PASSEPORT, passeport);
        session.setAttribute(SESSION_NUMERO_REFERENCE_VISA, form.get("numeroReferenceVisa"));
        session.setAttribute(SESSION_DATE_EXPIRATION_VISA, dateExpirationVisaParsee);
        if (dateExpirationVisaParsee != null && dateExpirationVisaParsee.isBefore(LocalDate.now())) {
            session.setAttribute(SESSION_AVERTISSEMENT_VISA,
                    "Attention: le visa transformable semble deja expire a la saisie.");
        } else {
            session.removeAttribute(SESSION_AVERTISSEMENT_VISA);
        }
        return "redirect:/demande/nouveau/etape3a";
    }

    @GetMapping("/nouveau/etape3a")
    public String afficherEtape3a(Model model, HttpSession session) {
        if (session.getAttribute(SESSION_DEMANDEUR) == null || session.getAttribute(SESSION_PASSEPORT) == null) {
            return "redirect:/demande/nouveau/etape1";
        }

        List<TypeVisa> typesVisa = typeVisaService.findAll();
        List<TypePieceCommune> piecesCommunes = typePieceCommuneService.findAll();

        model.addAttribute("typesVisa", typesVisa);
        model.addAttribute("piecesCommunes", piecesCommunes);
        model.addAttribute("typeVisaId", session.getAttribute(SESSION_TYPE_VISA_ID));
        Object dateDemande = session.getAttribute(SESSION_DATE_DEMANDE);
        model.addAttribute("dateDemande", dateDemande == null ? LocalDate.now().toString() : String.valueOf(dateDemande));
        model.addAttribute("avertissementVisa", session.getAttribute(SESSION_AVERTISSEMENT_VISA));
        model.addAttribute("piecesCommunesSelection", lireMapSession(session, SESSION_PIECES_COMMUNES));
        model.addAttribute("erreurs", Collections.emptyMap());
        return "demande/etape3a";
    }

    @PostMapping("/nouveau/etape3a")
    public String traiterEtape3a(
            @RequestParam(required = false) String typeVisaId,
            @RequestParam(required = false) String dateDemande,
            @RequestParam Map<String, String> params,
            Model model,
            HttpSession session) {

        if (session.getAttribute(SESSION_DEMANDEUR) == null || session.getAttribute(SESSION_PASSEPORT) == null) {
            return "redirect:/demande/nouveau/etape1";
        }

        Map<String, String> erreurs = new HashMap<>();
        Integer typeVisaIdValue = toInteger(typeVisaId, "typeVisaId", "Le type de visa est obligatoire.", erreurs);
        Optional<TypeVisa> typeVisa = typeVisaIdValue == null ? Optional.empty() : typeVisaService.findById(typeVisaIdValue);
        if (typeVisaIdValue != null && typeVisa.isEmpty()) {
            erreurs.put("typeVisaId", "Le type de visa selectionne est introuvable.");
        }

        LocalDate dateDemandeParsee = null;
        if (valeur(dateDemande).isEmpty()) {
            erreurs.put("dateDemande", "La date de la demande est obligatoire.");
        } else {
            try {
                dateDemandeParsee = LocalDate.parse(dateDemande);
            } catch (DateTimeParseException exception) {
                erreurs.put("dateDemande", "La date de la demande est invalide.");
            }
        }

        List<TypePieceCommune> piecesCommunes = typePieceCommuneService.findAll();
        Map<Integer, Boolean> selection = new LinkedHashMap<>();
        for (TypePieceCommune piece : piecesCommunes) {
            String key = "pieceCommune_" + piece.getId();
            selection.put(piece.getId(), params.containsKey(key));
        }

        if (!erreurs.isEmpty()) {
            model.addAttribute("typesVisa", typeVisaService.findAll());
            model.addAttribute("piecesCommunes", piecesCommunes);
            model.addAttribute("typeVisaId", typeVisaId);
            model.addAttribute("dateDemande", dateDemande);
            model.addAttribute("piecesCommunesSelection", selection);
            model.addAttribute("erreurs", erreurs);
            return "demande/etape3a";
        }

        session.setAttribute(SESSION_TYPE_VISA_ID, typeVisaIdValue);
        session.setAttribute(SESSION_DATE_DEMANDE, dateDemandeParsee);
        session.setAttribute(SESSION_PIECES_COMMUNES, selection);
        return "redirect:/demande/nouveau/etape3b";
    }

    @GetMapping("/nouveau/etape3b")
    public String afficherEtape3b(Model model, HttpSession session) {
        Integer typeVisaId = (Integer) session.getAttribute(SESSION_TYPE_VISA_ID);
        if (session.getAttribute(SESSION_DEMANDEUR) == null || session.getAttribute(SESSION_PASSEPORT) == null || typeVisaId == null) {
            return "redirect:/demande/nouveau/etape1";
        }

        Optional<TypeVisa> typeVisa = typeVisaService.findById(typeVisaId);
        if (typeVisa.isEmpty()) {
            return "redirect:/demande/nouveau/etape3a";
        }

        List<TypePieceSpecifique> piecesSpecifiques = filtrerPiecesSpecifiquesParTypeVisa(typeVisaId);
        model.addAttribute("typeVisa", typeVisa.get());
        model.addAttribute("piecesSpecifiques", piecesSpecifiques);
        model.addAttribute("piecesSpecifiquesSelection", lireMapSession(session, SESSION_PIECES_SPECIFIQUES));
        model.addAttribute("erreurs", Collections.emptyMap());
        return "demande/etape3b";
    }

    @PostMapping("/nouveau/etape3b")
    public String traiterEtape3b(
            @RequestParam Map<String, String> params,
            Model model,
            HttpSession session) {

        Integer typeVisaId = (Integer) session.getAttribute(SESSION_TYPE_VISA_ID);
        if (session.getAttribute(SESSION_DEMANDEUR) == null || session.getAttribute(SESSION_PASSEPORT) == null || typeVisaId == null) {
            return "redirect:/demande/nouveau/etape1";
        }

        Optional<TypeVisa> typeVisa = typeVisaService.findById(typeVisaId);
        if (typeVisa.isEmpty()) {
            return "redirect:/demande/nouveau/etape3a";
        }

        List<TypePieceSpecifique> piecesSpecifiques = filtrerPiecesSpecifiquesParTypeVisa(typeVisaId);
        Map<Integer, Boolean> selection = new LinkedHashMap<>();
        for (TypePieceSpecifique piece : piecesSpecifiques) {
            String key = "pieceSpecifique_" + piece.getId();
            selection.put(piece.getId(), params.containsKey(key));
        }

        session.setAttribute(SESSION_PIECES_SPECIFIQUES, selection);
        return "redirect:/demande/nouveau/etape4";
    }

    @GetMapping("/nouveau/etape4")
    public String afficherEtape4(Model model, HttpSession session) {
        if (!chargerResumeDepuisSession(model, session)) {
            return "redirect:/demande/nouveau/etape1";
        }

        model.addAttribute("erreurs", Collections.emptyMap());
        return "demande/etape4";
    }

    @PostMapping("/nouveau/etape4")
    public String traiterEtape4(Model model, HttpSession session) {
        if (!chargerResumeDepuisSession(model, session)) {
            return "redirect:/demande/nouveau/etape1";
        }

        String numeroReferenceVisa = (String) session.getAttribute(SESSION_NUMERO_REFERENCE_VISA);
        LocalDate dateExpirationVisa = (LocalDate) session.getAttribute(SESSION_DATE_EXPIRATION_VISA);
        LocalDate dateDemande = (LocalDate) session.getAttribute(SESSION_DATE_DEMANDE);

        if (dateExpirationVisa != null && dateDemande != null && dateExpirationVisa.isBefore(dateDemande)) {
            model.addAttribute(
                    "erreurVisa",
                    "Le visa transformable est expire a la date de la demande. Date d'expiration : "
                            + dateExpirationVisa + " / Date de demande : " + dateDemande);
            model.addAttribute("erreurs", Collections.emptyMap());
            return "demande/etape4";
        }

        Demandeur demandeur = (Demandeur) session.getAttribute(SESSION_DEMANDEUR);
        Passeport passeport = (Passeport) session.getAttribute(SESSION_PASSEPORT);
        Integer typeVisaId = (Integer) session.getAttribute(SESSION_TYPE_VISA_ID);
        Map<Integer, Boolean> piecesCommunesSelection = lireMapSession(session, SESSION_PIECES_COMMUNES);
        Map<Integer, Boolean> piecesSpecifiquesSelection = lireMapSession(session, SESSION_PIECES_SPECIFIQUES);

        Optional<TypeVisa> typeVisa = typeVisaService.findById(typeVisaId);
        Optional<TypeDemande> typeDemande = typeDemandeService.findById(1);
        Optional<StatutDemandeType> statutDemandeType = statutDemandeTypeService.findById(1);

        Map<String, String> erreurs = new HashMap<>();
        if (typeVisa.isEmpty()) {
            erreurs.put("global", "Le type de visa selectionne est introuvable.");
        }
        if (typeDemande.isEmpty()) {
            erreurs.put("global", "La reference Type_demande id=1 est manquante.");
        }
        if (statutDemandeType.isEmpty()) {
            erreurs.put("global", "La reference Statut_demande_type id=1 est manquante.");
        }
        if (numeroReferenceVisa == null || numeroReferenceVisa.isBlank()) {
            erreurs.put("global", "Le numero de reference du visa transformable est obligatoire.");
        }
        if (dateExpirationVisa == null) {
            erreurs.put("global", "La date d'expiration du visa transformable est obligatoire.");
        }
        if (dateDemande == null) {
            erreurs.put("global", "La date de la demande est obligatoire.");
        }

        if (!erreurs.isEmpty()) {
            model.addAttribute("erreurs", erreurs);
            return "demande/etape4";
        }

        try {
            Demandeur savedDemandeur = demandeurService.save(demandeur);

            passeport.setDemandeur(savedDemandeur);
            Passeport savedPasseport = passeportService.save(passeport);

            Optional<VisaTransformable> visaExistant = visaTransformableService.findByNumeroReference(numeroReferenceVisa);
            VisaTransformable visaTransformable;
            if (visaExistant.isPresent()) {
                visaTransformable = visaExistant.get();
            } else {
                VisaTransformable nouveauVisa = new VisaTransformable();
                nouveauVisa.setDemandeur(savedDemandeur);
                nouveauVisa.setPasseport(savedPasseport);
                nouveauVisa.setNumeroReference(numeroReferenceVisa);
                nouveauVisa.setDateExpiration(dateExpirationVisa);
                visaTransformable = visaTransformableService.save(nouveauVisa);
            }

            Demande demande = new Demande();
            demande.setDateDemande(dateDemande);
            demande.setDemandeur(savedDemandeur);
            demande.setTypeVisa(typeVisa.get());
            demande.setTypeDemande(typeDemande.get());
            demande.setVisaTransformable(visaTransformable);
            Demande savedDemande = demandeService.save(demande);

            StatutDemande statutDemande = new StatutDemande();
            statutDemande.setDemande(savedDemande);
            statutDemande.setStatutDemandeType(statutDemandeType.get());
            statutDemande.setDateChangement(LocalDateTime.now());
            statutDemandeService.save(statutDemande);

            for (Map.Entry<Integer, Boolean> entry : piecesCommunesSelection.entrySet()) {
                Optional<TypePieceCommune> typePieceCommune = typePieceCommuneService.findById(entry.getKey());
                if (typePieceCommune.isEmpty()) {
                    continue;
                }
                PieceDemande pieceDemande = new PieceDemande();
                pieceDemande.setDemande(savedDemande);
                pieceDemande.setTypePieceCommune(typePieceCommune.get());
                pieceDemande.setPresente(entry.getValue());
                pieceDemandeService.save(pieceDemande);
            }

            for (Map.Entry<Integer, Boolean> entry : piecesSpecifiquesSelection.entrySet()) {
                Optional<TypePieceSpecifique> typePieceSpecifique = typePieceSpecifiqueService.findById(entry.getKey());
                if (typePieceSpecifique.isEmpty()) {
                    continue;
                }
                PieceDemandeSpecifique pieceDemandeSpecifique = new PieceDemandeSpecifique();
                pieceDemandeSpecifique.setDemande(savedDemande);
                pieceDemandeSpecifique.setTypePiece(typePieceSpecifique.get());
                pieceDemandeSpecifique.setPresente(entry.getValue());
                pieceDemandeSpecifiqueService.save(pieceDemandeSpecifique);
            }

            session.invalidate();
            return "redirect:/demande/confirmation/" + savedDemande.getId();
        } catch (RuntimeException exception) {
            erreurs.put("global", "Erreur lors de la sauvegarde: " + exception.getMessage());
            model.addAttribute("erreurs", erreurs);
            return "demande/etape4";
        }
    }

    @GetMapping("/confirmation/{id}")
    public String confirmation(@PathVariable Integer id, Model model) {
        model.addAttribute("numeroDossier", id);
        return "demande/confirmation";
    }

    private void initialiserModeleEtape1(Model model) {
        model.addAttribute("nationalites", nationaliteService.findAll());
        model.addAttribute("situationsFamiliales", situationFamilialeService.findAll());
    }

    private Map<String, String> formEtape1DepuisSession(Demandeur demandeur) {
        Map<String, String> form = new HashMap<>();
        if (demandeur == null) {
            return form;
        }

        form.put("nom", demandeur.getNom());
        form.put("prenom", demandeur.getPrenom());
        form.put("dateNaissance", demandeur.getDateNaissance() == null ? "" : demandeur.getDateNaissance().toString());
        form.put("lieuNaissance", demandeur.getLieuNaissance());
        form.put("telephone", demandeur.getTelephone());
        form.put("email", demandeur.getEmail());
        form.put("adresse", demandeur.getAdresse());
        form.put("idNationalite", demandeur.getNationalite() == null ? "" : String.valueOf(demandeur.getNationalite().getId()));
        form.put("idSituationFamiliale", demandeur.getSituationFamiliale() == null ? "" : String.valueOf(demandeur.getSituationFamiliale().getId()));
        return form;
    }

    private Map<String, String> formEtape2DepuisSession(Passeport passeport) {
        Map<String, String> form = new HashMap<>();
        if (passeport == null) {
            return form;
        }

        form.put("numeroPasseport", passeport.getNumeroPasseport());
        form.put("dateDelivrance", passeport.getDateDelivrance() == null ? "" : passeport.getDateDelivrance().toString());
        form.put("dateExpiration", passeport.getDateExpiration() == null ? "" : passeport.getDateExpiration().toString());
        form.put("paysDelivrance", passeport.getPaysDelivrance());
        return form;
    }

    private Integer toInteger(String value, String key, String requiredMessage, Map<String, String> erreurs) {
        if (valeur(value).isEmpty()) {
            erreurs.put(key, requiredMessage);
            return null;
        }
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException exception) {
            erreurs.put(key, "Valeur numerique invalide.");
            return null;
        }
    }

    private String valeur(String value) {
        return value == null ? "" : value.trim();
    }

    @SuppressWarnings("unchecked")
    private Map<Integer, Boolean> lireMapSession(HttpSession session, String sessionKey) {
        Object value = session.getAttribute(sessionKey);
        if (value instanceof Map<?, ?> map) {
            return (Map<Integer, Boolean>) map;
        }
        return new LinkedHashMap<>();
    }

    private List<TypePieceSpecifique> filtrerPiecesSpecifiquesParTypeVisa(Integer typeVisaId) {
        return typePieceSpecifiqueService.findAll().stream()
                .filter(piece -> piece.getTypeVisa() != null && typeVisaId.equals(piece.getTypeVisa().getId()))
                .collect(Collectors.toList());
    }

    private boolean chargerResumeDepuisSession(Model model, HttpSession session) {
        Demandeur demandeur = (Demandeur) session.getAttribute(SESSION_DEMANDEUR);
        Passeport passeport = (Passeport) session.getAttribute(SESSION_PASSEPORT);
        Integer typeVisaId = (Integer) session.getAttribute(SESSION_TYPE_VISA_ID);
        String numeroReferenceVisa = (String) session.getAttribute(SESSION_NUMERO_REFERENCE_VISA);
        LocalDate dateExpirationVisa = (LocalDate) session.getAttribute(SESSION_DATE_EXPIRATION_VISA);
        LocalDate dateDemande = (LocalDate) session.getAttribute(SESSION_DATE_DEMANDE);

        if (demandeur == null || passeport == null || typeVisaId == null || numeroReferenceVisa == null
                || dateExpirationVisa == null || dateDemande == null) {
            return false;
        }

        Optional<TypeVisa> typeVisa = typeVisaService.findById(typeVisaId);
        if (typeVisa.isEmpty()) {
            return false;
        }

        List<TypePieceCommune> typesPiecesCommunes = typePieceCommuneService.findAll();
        List<TypePieceSpecifique> typesPiecesSpecifiques = filtrerPiecesSpecifiquesParTypeVisa(typeVisaId);

        model.addAttribute("demandeur", demandeur);
        model.addAttribute("passeport", passeport);
        model.addAttribute("typeVisa", typeVisa.get());
        model.addAttribute("piecesCommunesSelection", lireMapSession(session, SESSION_PIECES_COMMUNES));
        model.addAttribute("piecesSpecifiquesSelection", lireMapSession(session, SESSION_PIECES_SPECIFIQUES));
        model.addAttribute("typesPiecesCommunes", typesPiecesCommunes);
        model.addAttribute("typesPiecesSpecifiques", typesPiecesSpecifiques);
        model.addAttribute("numeroReferenceVisa", numeroReferenceVisa);
        model.addAttribute("dateExpirationVisa", dateExpirationVisa);
        model.addAttribute("dateDemande", dateDemande);

        if (demandeur.getDateNaissance() != null) {
            model.addAttribute("dateNaissanceDate", Date.valueOf(demandeur.getDateNaissance()));
        }
        if (passeport.getDateDelivrance() != null) {
            model.addAttribute("dateDelivranceDate", Date.valueOf(passeport.getDateDelivrance()));
        }
        if (passeport.getDateExpiration() != null) {
            model.addAttribute("dateExpirationDate", Date.valueOf(passeport.getDateExpiration()));
        }
        return true;
    }

    private void viderSession(HttpSession session) {
        session.removeAttribute(SESSION_DEMANDEUR);
        session.removeAttribute(SESSION_PASSEPORT);
        session.removeAttribute(SESSION_TYPE_VISA_ID);
        session.removeAttribute(SESSION_PIECES_COMMUNES);
        session.removeAttribute(SESSION_PIECES_SPECIFIQUES);
        session.removeAttribute(SESSION_NUMERO_REFERENCE_VISA);
        session.removeAttribute(SESSION_DATE_EXPIRATION_VISA);
        session.removeAttribute(SESSION_DATE_DEMANDE);
    }
}