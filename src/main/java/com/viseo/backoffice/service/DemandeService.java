package com.viseo.backoffice.service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.viseo.backoffice.model.CarteResident;
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
import com.viseo.backoffice.model.Visa;
import com.viseo.backoffice.model.VisaTransformable;
import com.viseo.backoffice.repository.StatutDemandeRepository;
import com.viseo.backoffice.repository.StatutDemandeTypeRepository;
import com.viseo.backoffice.repository.DemandeRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.persistence.EntityNotFoundException;

@Service
public class DemandeService {

    private static final Logger log = LoggerFactory.getLogger(DemandeService.class);

    private static final String SESSION_DEMANDEUR = "demandeur";
    private static final String SESSION_PASSEPORT = "passeport";
    private static final String SESSION_TYPE_VISA_ID = "typeVisaId";
    private static final String SESSION_PIECES_COMMUNES = "piecesCommunes";
    private static final String SESSION_PIECES_SPECIFIQUES = "piecesSpecifiques";
    private static final String SESSION_NUMERO_REFERENCE_VISA = "numeroReferenceVisa";
    private static final String SESSION_DATE_EXPIRATION_VISA = "dateExpirationVisa";
    private static final String SESSION_DATE_DEMANDE = "dateDemande";
    private static final String SESSION_AVERTISSEMENT_VISA = "avertissementVisa";

    private final DemandeRepository demandeRepository;
    private final StatutDemandeRepository statutDemandeRepository;
    private final StatutDemandeTypeRepository statutDemandeTypeRepository;
    private final NationaliteService nationaliteService;
    private final SituationFamilialeService situationFamilialeService;
    private final TypeVisaService typeVisaService;
    private final TypePieceCommuneService typePieceCommuneService;
    private final TypePieceSpecifiqueService typePieceSpecifiqueService;
    private final DemandeurService demandeurService;
    private final PasseportService passeportService;
    private final StatutDemandeService statutDemandeService;
    private final PieceDemandeService pieceDemandeService;
    private final PieceDemandeSpecifiqueService pieceDemandeSpecifiqueService;
    private final VisaTransformableService visaTransformableService;
    private final TypeDemandeService typeDemandeService;
    private final StatutDemandeTypeService statutDemandeTypeService;

    public DemandeService(
            DemandeRepository demandeRepository,
            StatutDemandeRepository statutDemandeRepository,
            StatutDemandeTypeRepository statutDemandeTypeRepository,
            NationaliteService nationaliteService,
            SituationFamilialeService situationFamilialeService,
            TypeVisaService typeVisaService,
            TypePieceCommuneService typePieceCommuneService,
            TypePieceSpecifiqueService typePieceSpecifiqueService,
            DemandeurService demandeurService,
            PasseportService passeportService,
            StatutDemandeService statutDemandeService,
            PieceDemandeService pieceDemandeService,
            PieceDemandeSpecifiqueService pieceDemandeSpecifiqueService,
            VisaTransformableService visaTransformableService,
            TypeDemandeService typeDemandeService,
            StatutDemandeTypeService statutDemandeTypeService) {
        this.demandeRepository = demandeRepository;
        this.statutDemandeRepository = statutDemandeRepository;
        this.statutDemandeTypeRepository = statutDemandeTypeRepository;
        this.nationaliteService = nationaliteService;
        this.situationFamilialeService = situationFamilialeService;
        this.typeVisaService = typeVisaService;
        this.typePieceCommuneService = typePieceCommuneService;
        this.typePieceSpecifiqueService = typePieceSpecifiqueService;
        this.demandeurService = demandeurService;
        this.passeportService = passeportService;
        this.statutDemandeService = statutDemandeService;
        this.pieceDemandeService = pieceDemandeService;
        this.pieceDemandeSpecifiqueService = pieceDemandeSpecifiqueService;
        this.visaTransformableService = visaTransformableService;
        this.typeDemandeService = typeDemandeService;
        this.statutDemandeTypeService = statutDemandeTypeService;
    }

    public List<Demande> findAll() {
        return demandeRepository.findAll();
    }

    public Demande findById(Integer id) {
        return demandeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Demande introuvable: " + id));
    }

    public Optional<Demande> findOptionalById(Integer id) {
        return demandeRepository.findById(id);
    }

    public Demande findByVisa(Visa visa) {
        if (visa == null || visa.getDemande() == null || visa.getDemande().getId() == null) {
            throw new EntityNotFoundException("Demande introuvable pour le visa.");
        }
        return findById(visa.getDemande().getId());
    }

    public Demande findByCarteResident(CarteResident carte) {
        if (carte == null || carte.getDemande() == null || carte.getDemande().getId() == null) {
            throw new EntityNotFoundException("Demande introuvable pour la carte de resident.");
        }
        return findById(carte.getDemande().getId());
    }

    @Transactional
    public Demande creerDemandeDepuisOrigine(
            Demande demandeOrigine,
            LocalDate nouvelleDateDemande,
            Integer nouveauIdTypeDemande,
            Integer idStatutInitial) {
        TypeDemande nouveauTypeDemande = typeDemandeService.findById(nouveauIdTypeDemande);

        Demande nouvelleDemande = new Demande();
        nouvelleDemande.setVisaTransformable(demandeOrigine.getVisaTransformable());
        nouvelleDemande.setDateDemande(nouvelleDateDemande);
        nouvelleDemande.setDemandeur(demandeOrigine.getDemandeur());
        nouvelleDemande.setTypeVisa(demandeOrigine.getTypeVisa());
        nouvelleDemande.setTypeDemande(nouveauTypeDemande);

        Demande demandeSauvegardee = demandeRepository.save(nouvelleDemande);
        changerStatut(demandeSauvegardee, idStatutInitial);
        return demandeSauvegardee;
    }

    public void changerStatut(Demande demande, Integer idStatutType) {
        StatutDemandeType statutType = statutDemandeTypeRepository.findById(idStatutType)
                .orElseThrow(() -> new EntityNotFoundException("Statut de demande introuvable: " + idStatutType));

        StatutDemande statut = new StatutDemande();
        statut.setDemande(demande);
        statut.setStatutDemandeType(statutType);
        statut.setDateChangement(LocalDateTime.now());
        statutDemandeRepository.save(statut);
    }

    public Demande save(Demande entity) {
        return demandeRepository.save(entity);
    }

    public void deleteById(Integer id) {
        demandeRepository.deleteById(id);
    }

    public String afficherEtape1(Model model, HttpSession session) {
        initialiserModeleEtape1(model);

        Demandeur demandeur = (Demandeur) session.getAttribute(SESSION_DEMANDEUR);
        model.addAttribute("form", formEtape1DepuisSession(demandeur));
        model.addAttribute("erreurs", Collections.emptyMap());
        return "demande/etape1";
    }

    public String traiterEtape1(
            String nom,
            String prenom,
            String dateNaissance,
            String lieuNaissance,
            String telephone,
            String email,
            String adresse,
            String idNationalite,
            String idSituationFamiliale,
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

    public String traiterEtape2(
            String numeroPasseport,
            String dateDelivrance,
            String dateExpiration,
            String paysDelivrance,
            String numeroReferenceVisa,
            String dateExpirationVisa,
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

    public String traiterEtape3a(
            String typeVisaId,
            String dateDemande,
            Map<String, String> params,
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

    public String traiterEtape3b(
            Map<String, String> params,
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

    public String afficherEtape4(Model model, HttpSession session) {
        if (!chargerResumeDepuisSession(model, session)) {
            return "redirect:/demande/nouveau/etape1";
        }

        model.addAttribute("erreurs", Collections.emptyMap());
        return "demande/etape4";
    }

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

        List<String> piecesObligatoiresManquantes = new ArrayList<>();

        List<TypePieceCommune> toutesLesPiecesCommunes = typePieceCommuneService.findAll();
        Map<Integer, Boolean> piecesCommunes = lireMapSession(session, SESSION_PIECES_COMMUNES);
        for (TypePieceCommune piece : toutesLesPiecesCommunes) {
            if (Boolean.TRUE.equals(piece.getObligatoire())) {
                Boolean presente = piecesCommunes.get(piece.getId());
                if (presente == null || !presente) {
                    piecesObligatoiresManquantes.add("COMMUNE - " + piece.getLibelle());
                    log.warn("Piece commune obligatoire manquante : [id={}] {}", piece.getId(), piece.getLibelle());
                }
            }
        }

        Integer typeVisaId = (Integer) session.getAttribute(SESSION_TYPE_VISA_ID);
        List<TypePieceSpecifique> piecesSpecifiquesType = typeVisaId == null
                ? Collections.emptyList()
                : typePieceSpecifiqueService.findByTypeVisaId(typeVisaId);
        Map<Integer, Boolean> piecesSpecifiques = lireMapSession(session, SESSION_PIECES_SPECIFIQUES);
        for (TypePieceSpecifique piece : piecesSpecifiquesType) {
            if (Boolean.TRUE.equals(piece.getObligatoire())) {
                Boolean presente = piecesSpecifiques.get(piece.getId());
                if (presente == null || !presente) {
                    piecesObligatoiresManquantes.add("SPECIFIQUE - " + piece.getLibelle());
                    log.warn("Piece specifique obligatoire manquante : [id={}] {} (typeVisa={})",
                            piece.getId(), piece.getLibelle(), typeVisaId);
                }
            }
        }

        if (!piecesObligatoiresManquantes.isEmpty()) {
            log.error("INSERTION ANNULEE - Demande refusee pour pieces obligatoires manquantes. Nombre de pieces manquantes : {}. Detail : {}",
                    piecesObligatoiresManquantes.size(), String.join(" | ", piecesObligatoiresManquantes));

            model.addAttribute("erreurPieces",
                    "La demande ne peut pas etre soumise : " + piecesObligatoiresManquantes.size()
                            + " piece(s) obligatoire(s) non presentee(s).");
            model.addAttribute("listePiecesManquantes", piecesObligatoiresManquantes);

            model.addAttribute("demandeur", session.getAttribute(SESSION_DEMANDEUR));
            model.addAttribute("passeport", session.getAttribute(SESSION_PASSEPORT));
            model.addAttribute("numeroReferenceVisa", session.getAttribute(SESSION_NUMERO_REFERENCE_VISA));
            model.addAttribute("dateExpirationVisa", session.getAttribute(SESSION_DATE_EXPIRATION_VISA));
            model.addAttribute("dateDemande", session.getAttribute(SESSION_DATE_DEMANDE));
            model.addAttribute("typeVisaId", session.getAttribute(SESSION_TYPE_VISA_ID));
            model.addAttribute("piecesCommunes", session.getAttribute(SESSION_PIECES_COMMUNES));
            model.addAttribute("piecesSpecifiques", session.getAttribute(SESSION_PIECES_SPECIFIQUES));

            return "demande/etape4";
        }

        log.info("Verification des pieces obligatoires : OK - Poursuite de l'insertion.");

        Demandeur demandeur = (Demandeur) session.getAttribute(SESSION_DEMANDEUR);
        Passeport passeport = (Passeport) session.getAttribute(SESSION_PASSEPORT);
        Map<Integer, Boolean> piecesCommunesSelection = lireMapSession(session, SESSION_PIECES_COMMUNES);
        Map<Integer, Boolean> piecesSpecifiquesSelection = lireMapSession(session, SESSION_PIECES_SPECIFIQUES);

        Optional<TypeVisa> typeVisa = typeVisaService.findById(typeVisaId);
        Optional<TypeDemande> typeDemande = typeDemandeService.findOptionalById(1);
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
            Demande savedDemande = save(demande);

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

    public String confirmation(Integer id, Model model) {
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

    public List<Demande> searchDemandes(String query) {
        try {
            Integer id = Integer.parseInt(query);
            Optional<Demande> optionalDemande = demandeRepository.findById(id);
            if (optionalDemande.isPresent()) {
                Demande searched = optionalDemande.get();
                List<Demande> allFromDemandeur = demandeRepository.findByDemandeurId(searched.getDemandeur().getId());
                
                // Sort: Search ID first, then others by date descending
                List<Demande> result = new ArrayList<>();
                result.add(searched);
                allFromDemandeur.stream()
                    .filter(d -> !d.getId().equals(id))
                    .sorted((d1, d2) -> d2.getDateDemande().compareTo(d1.getDateDemande()))
                    .forEach(result::add);
                return result;
            }
        } catch (NumberFormatException e) {
            // Not an ID, try as Passport Number
        }

        return passeportService.findByNumeroPasseport(query)
            .map(p -> demandeRepository.findByDemandeurId(p.getDemandeur().getId())
                .stream()
                .sorted((d1, d2) -> d1.getDateDemande().compareTo(d2.getDateDemande()))
                .collect(Collectors.toList()))
            .orElse(Collections.emptyList());
    }
}
