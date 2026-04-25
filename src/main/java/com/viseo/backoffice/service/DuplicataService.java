package com.viseo.backoffice.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.viseo.backoffice.model.CarteResident;
import com.viseo.backoffice.model.Demande;
import com.viseo.backoffice.model.DemandeLiee;
import com.viseo.backoffice.model.Demandeur;
import com.viseo.backoffice.model.Nationalite;
import com.viseo.backoffice.model.Passeport;
import com.viseo.backoffice.model.PieceDemande;
import com.viseo.backoffice.model.PieceDemandeSpecifique;
import com.viseo.backoffice.model.SituationFamiliale;
import com.viseo.backoffice.model.StatutCarteResident;
import com.viseo.backoffice.model.StatutDemande;
import com.viseo.backoffice.model.StatutDemandeType;
import com.viseo.backoffice.model.StatutTitreType;
import com.viseo.backoffice.model.StatutVisa;
import com.viseo.backoffice.model.TypeDemande;
import com.viseo.backoffice.model.TypePieceCommune;
import com.viseo.backoffice.model.TypePieceSpecifique;
import com.viseo.backoffice.model.TypeVisa;
import com.viseo.backoffice.model.UploadPiece;
import com.viseo.backoffice.model.Visa;
import com.viseo.backoffice.model.VisaTransformable;
import com.viseo.backoffice.repository.CarteResidentRepository;
import com.viseo.backoffice.repository.DemandeLieeRepository;
import com.viseo.backoffice.repository.DemandeRepository;
import com.viseo.backoffice.repository.DemandeurRepository;
import com.viseo.backoffice.repository.NationaliteRepository;
import com.viseo.backoffice.repository.PasseportRepository;
import com.viseo.backoffice.repository.PieceDemandeRepository;
import com.viseo.backoffice.repository.PieceDemandeSpecifiqueRepository;
import com.viseo.backoffice.repository.SituationFamilialeRepository;
import com.viseo.backoffice.repository.StatutCarteResidentRepository;
import com.viseo.backoffice.repository.StatutDemandeRepository;
import com.viseo.backoffice.repository.StatutDemandeTypeRepository;
import com.viseo.backoffice.repository.StatutTitreTypeRepository;
import com.viseo.backoffice.repository.TypeDemandeRepository;
import com.viseo.backoffice.repository.TypePieceCommuneRepository;
import com.viseo.backoffice.repository.TypePieceSpecifiqueRepository;
import com.viseo.backoffice.repository.TypeVisaRepository;
import com.viseo.backoffice.repository.UploadPieceRepository;
import com.viseo.backoffice.repository.VisaTransformableRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class DuplicataService {

    private static final Logger log = LoggerFactory.getLogger(DuplicataService.class);

    private final DemandeurRepository demandeurRepository;
    private final PasseportRepository passeportRepository;
    private final DemandeRepository demandeRepository;
    private final StatutDemandeRepository statutDemandeRepository;
    private final StatutDemandeTypeRepository statutDemandeTypeRepository;
    private final PieceDemandeRepository pieceDemandeRepository;
    private final PieceDemandeSpecifiqueRepository pieceDemandeSpecifiqueRepository;
    private final TypePieceCommuneRepository typePieceCommuneRepository;
    private final TypePieceSpecifiqueRepository typePieceSpecifiqueRepository;
    private final CarteResidentRepository carteResidentRepository;
    private final StatutCarteResidentRepository statutCarteResidentRepository;
    private final StatutTitreTypeRepository statutTitreTypeRepository;
    private final DemandeLieeRepository demandeLieeRepository;
    private final VisaTransformableRepository visaTransformableRepository;
    private final UploadPieceRepository uploadPieceRepository;
    private final TypeVisaRepository typeVisaRepository;
    private final TypeDemandeRepository typeDemandeRepository;
    private final NationaliteRepository nationaliteRepository;
    private final SituationFamilialeRepository situationFamilialeRepository;
    private final TypeDemandeService typeDemandeService;
    private final VisaService visaService;
    private final CarteResidentService carteResidentService;
    private final DemandeService demandeService;
    private final DemandeLieeService demandeLieeService;

    @Value("${upload.path}")
    private String uploadPath;

    public DuplicataService(
            DemandeurRepository demandeurRepository,
            PasseportRepository passeportRepository,
            DemandeRepository demandeRepository,
            StatutDemandeRepository statutDemandeRepository,
            StatutDemandeTypeRepository statutDemandeTypeRepository,
            PieceDemandeRepository pieceDemandeRepository,
            PieceDemandeSpecifiqueRepository pieceDemandeSpecifiqueRepository,
            TypePieceCommuneRepository typePieceCommuneRepository,
            TypePieceSpecifiqueRepository typePieceSpecifiqueRepository,
            CarteResidentRepository carteResidentRepository,
            StatutCarteResidentRepository statutCarteResidentRepository,
            StatutTitreTypeRepository statutTitreTypeRepository,
            DemandeLieeRepository demandeLieeRepository,
            VisaTransformableRepository visaTransformableRepository,
            UploadPieceRepository uploadPieceRepository,
            TypeVisaRepository typeVisaRepository,
            TypeDemandeRepository typeDemandeRepository,
            NationaliteRepository nationaliteRepository,
            SituationFamilialeRepository situationFamilialeRepository,
            TypeDemandeService typeDemandeService,
            VisaService visaService,
            CarteResidentService carteResidentService,
            DemandeService demandeService,
            DemandeLieeService demandeLieeService) {
        this.demandeurRepository = demandeurRepository;
        this.passeportRepository = passeportRepository;
        this.demandeRepository = demandeRepository;
        this.statutDemandeRepository = statutDemandeRepository;
        this.statutDemandeTypeRepository = statutDemandeTypeRepository;
        this.pieceDemandeRepository = pieceDemandeRepository;
        this.pieceDemandeSpecifiqueRepository = pieceDemandeSpecifiqueRepository;
        this.typePieceCommuneRepository = typePieceCommuneRepository;
        this.typePieceSpecifiqueRepository = typePieceSpecifiqueRepository;
        this.carteResidentRepository = carteResidentRepository;
        this.statutCarteResidentRepository = statutCarteResidentRepository;
        this.statutTitreTypeRepository = statutTitreTypeRepository;
        this.demandeLieeRepository = demandeLieeRepository;
        this.visaTransformableRepository = visaTransformableRepository;
        this.uploadPieceRepository = uploadPieceRepository;
        this.typeVisaRepository = typeVisaRepository;
        this.typeDemandeRepository = typeDemandeRepository;
        this.nationaliteRepository = nationaliteRepository;
        this.situationFamilialeRepository = situationFamilialeRepository;
        this.typeDemandeService = typeDemandeService;
        this.visaService = visaService;
        this.carteResidentService = carteResidentService;
        this.demandeService = demandeService;
        this.demandeLieeService = demandeLieeService;
    }

    public Optional<Demandeur> rechercherDemandeur(String nom, String prenom, String numeroPasseport) {
        Optional<Demandeur> demandeur = demandeurRepository.findByNomIgnoreCaseAndPrenomIgnoreCase(nom, prenom);
        if (demandeur.isEmpty()) {
            return Optional.empty();
        }
        List<Passeport> passeports = passeportRepository.findByDemandeur(demandeur.get());
        return passeports.stream()
                .anyMatch(p -> numeroPasseport.equalsIgnoreCase(p.getNumeroPasseport()))
                ? demandeur
                : Optional.empty();
    }

    public Optional<Passeport> findPasseportByNumero(String numeroPasseport) {
        return passeportRepository.findByNumeroPasseport(numeroPasseport);
    }

    public Demandeur findDemandeurById(Integer id) {
        return demandeurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Demandeur introuvable: " + id));
    }

    public Passeport findPasseportById(Integer id) {
        return passeportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Passeport introuvable: " + id));
    }

    public Demande findDemandeById(Integer id) {
        return demandeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Demande introuvable: " + id));
    }

    public CarteResident findCarteResidentById(Integer id) {
        return carteResidentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Carte resident introuvable: " + id));
    }

    public StatutTitreType findStatutTitreTypeById(Integer id) {
        return statutTitreTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Statut titre type introuvable: " + id));
    }

    public List<Nationalite> findAllNationalites() {
        return nationaliteRepository.findAll();
    }

    public List<SituationFamiliale> findAllSituationsFamiliales() {
        return situationFamilialeRepository.findAll();
    }

    public List<TypeVisa> findAllTypesVisa() {
        return typeVisaRepository.findAll();
    }

    public String findTypeVisaLibelleById(Integer id) {
        if (id == null) {
            return "";
        }
        return typeVisaRepository.findById(id)
                .map(TypeVisa::getLibelle)
                .orElse("");
    }

    public List<TypePieceCommune> findAllPiecesCommunes() {
        return typePieceCommuneRepository.findAll();
    }

    public List<TypePieceSpecifique> findPiecesSpecifiquesByTypeVisa(Integer idTypeVisa) {
        if (idTypeVisa == null) {
            return List.of();
        }
        return typePieceSpecifiqueRepository.findByIdTypeVisa(idTypeVisa);
    }

    public List<PieceDemande> findPiecesCommunesByDemande(Demande demande) {
        return pieceDemandeRepository.findByDemande(demande);
    }

    public List<PieceDemandeSpecifique> findPiecesSpecifiquesByDemande(Demande demande) {
        return pieceDemandeSpecifiqueRepository.findByDemande(demande);
    }

    public Map<Integer, UploadPiece> getDerniersUploadsCommunes(List<PieceDemande> pieces) {
        Map<Integer, UploadPiece> map = new HashMap<>();
        for (PieceDemande piece : pieces) {
            uploadPieceRepository.findFirstByPieceDemandeOrderByDateUploadDesc(piece)
                    .ifPresent(upload -> map.put(piece.getId(), upload));
        }
        return map;
    }

    public Map<Integer, UploadPiece> getDerniersUploadsSpecifiques(List<PieceDemandeSpecifique> pieces) {
        Map<Integer, UploadPiece> map = new HashMap<>();
        for (PieceDemandeSpecifique piece : pieces) {
            uploadPieceRepository.findFirstByPieceDemandeSpecifiqueOrderByDateUploadDesc(piece)
                    .ifPresent(upload -> map.put(piece.getId(), upload));
        }
        return map;
    }

    public Demandeur construireDemandeur(
            String nom,
            String prenom,
            LocalDate dateNaissance,
            String lieuNaissance,
            String telephone,
            String email,
            String adresse,
            Integer idNationalite,
            Integer idSituationFamiliale) {
        Demandeur d = new Demandeur();
        d.setNom(nom);
        d.setPrenom(prenom);
        d.setDateNaissance(dateNaissance);
        d.setLieuNaissance(lieuNaissance);
        d.setTelephone(telephone);
        d.setEmail(email);
        d.setAdresse(adresse);
        d.setNationalite(nationaliteRepository.findById(idNationalite)
                .orElseThrow(() -> new EntityNotFoundException("Nationalite introuvable: " + idNationalite)));
        d.setSituationFamiliale(situationFamilialeRepository.findById(idSituationFamiliale)
                .orElseThrow(() -> new EntityNotFoundException("Situation familiale introuvable: " + idSituationFamiliale)));
        return d;
    }

    public Passeport construirePasseport(Demandeur demandeur, Map<String, Object> passeportData) {
        Passeport p = new Passeport();
        p.setDemandeur(demandeur);
        p.setNumeroPasseport((String) passeportData.get("numero"));
        p.setDateDelivrance((LocalDate) passeportData.get("dateDelivrance"));
        p.setDateExpiration((LocalDate) passeportData.get("dateExpiration"));
        p.setPaysDelivrance((String) passeportData.get("paysDelivrance"));
        return p;
    }

    public VisaTransformable obtenirOuCreerVisaTransformable(
            String numeroReference,
            LocalDate dateExpiration,
            Demandeur demandeur,
            Passeport passeport) {
        return visaTransformableRepository.findByNumeroReference(numeroReference)
                .orElseGet(() -> {
                    VisaTransformable v = new VisaTransformable();
                    v.setNumeroReference(numeroReference);
                    v.setDateExpiration(dateExpiration);
                    v.setDemandeur(demandeur);
                    v.setPasseport(passeport);
                    return visaTransformableRepository.save(v);
                });
    }

    public boolean visaTransformableValide(LocalDate dateExpiration, LocalDate dateDemande) {
        if (dateExpiration == null || dateDemande == null) {
            return false;
        }
        return dateExpiration.isAfter(dateDemande);
    }

    public List<String> getPiecesObligatoiresNonUploadees(Demande demande) {
        List<String> manquantes = new ArrayList<>();

        List<PieceDemande> communes = pieceDemandeRepository.findByDemande(demande);
        for (PieceDemande piece : communes) {
            if (Boolean.TRUE.equals(piece.getPresente())
                    && Boolean.TRUE.equals(piece.getTypePieceCommune().getObligatoire())
                    && !Boolean.TRUE.equals(piece.getUploaded())) {
                manquantes.add(piece.getTypePieceCommune().getLibelle());
            }
        }

        List<PieceDemandeSpecifique> specifiques = pieceDemandeSpecifiqueRepository.findByDemande(demande);
        for (PieceDemandeSpecifique piece : specifiques) {
            if (Boolean.TRUE.equals(piece.getPresente())
                    && Boolean.TRUE.equals(piece.getTypePiece().getObligatoire())
                    && !Boolean.TRUE.equals(piece.getUploaded())) {
                manquantes.add(piece.getTypePiece().getLibelle());
            }
        }

        return manquantes;
    }

    public boolean peutContinuerApresUpload(Demande demande) {
        return getPiecesObligatoiresNonUploadees(demande).isEmpty();
    }

    public void traiterUpload(
            Integer idDemande,
            Integer idPiece,
            String typePiece,
            MultipartFile fichier,
            LocalDate dateUpload) {
        if (fichier == null || fichier.isEmpty()) {
            throw new IllegalArgumentException("Fichier vide.");
        }

        String nomOriginal = fichier.getOriginalFilename() == null ? "fichier" : fichier.getOriginalFilename();
        String ext = extension(nomOriginal);
        if (!isTypeAutorise(ext)) {
            throw new IllegalArgumentException("Type de fichier non autorise. Utilisez jpg, jpeg, png ou pdf.");
        }

        PieceDemande pieceCommune = null;
        PieceDemandeSpecifique pieceSpecifique = null;

        if ("commune".equalsIgnoreCase(typePiece)) {
            pieceCommune = pieceDemandeRepository.findById(idPiece)
                    .orElseThrow(() -> new EntityNotFoundException("Piece commune introuvable: " + idPiece));
            if (!pieceCommune.getDemande().getId().equals(idDemande)) {
                throw new IllegalArgumentException("La piece ne correspond pas a la demande active.");
            }
        } else if ("specifique".equalsIgnoreCase(typePiece)) {
            pieceSpecifique = pieceDemandeSpecifiqueRepository.findById(idPiece)
                    .orElseThrow(() -> new EntityNotFoundException("Piece specifique introuvable: " + idPiece));
            if (!pieceSpecifique.getDemande().getId().equals(idDemande)) {
                throw new IllegalArgumentException("La piece ne correspond pas a la demande active.");
            }
        } else {
            throw new IllegalArgumentException("Type de piece invalide.");
        }

        try {
            Path dossier = Paths.get(uploadPath, String.valueOf(idDemande));
            Files.createDirectories(dossier);

            String nomNettoye = nomOriginal.replaceAll("[^a-zA-Z0-9._-]", "_");
            String nomFichier = typePiece.toLowerCase(Locale.ROOT) + "_" + idPiece + "_" + nomNettoye;
            Path destination = dossier.resolve(nomFichier);
            Files.copy(fichier.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

            UploadPiece upload = new UploadPiece();
            upload.setCheminFichier(destination.toString());
            upload.setNomFichierOriginal(nomOriginal);
            upload.setDateUpload(dateUpload == null ? LocalDate.now() : dateUpload);
            upload.setPieceDemande(pieceCommune);
            upload.setPieceDemandeSpecifique(pieceSpecifique);
            uploadPieceRepository.save(upload);

            if (pieceCommune != null) {
                pieceCommune.setUploaded(true);
                pieceDemandeRepository.save(pieceCommune);
            }
            if (pieceSpecifique != null) {
                pieceSpecifique.setUploaded(true);
                pieceDemandeSpecifiqueRepository.save(pieceSpecifique);
            }
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la sauvegarde du fichier.", e);
        }
    }

    public void changerStatutDemande(Demande demande, Integer idStatutType) {
        StatutDemandeType type = statutDemandeTypeRepository.findById(idStatutType)
                .orElseThrow(() -> new EntityNotFoundException("StatutDemandeType introuvable: " + idStatutType));
        StatutDemande statut = new StatutDemande();
        statut.setDemande(demande);
        statut.setStatutDemandeType(type);
        statut.setDateChangement(LocalDateTime.now());
        statutDemandeRepository.save(statut);
    }

    public void changerStatutCarte(CarteResident carte, Integer idStatutType, String commentaire) {
        StatutTitreType type = statutTitreTypeRepository.findById(idStatutType)
                .orElseThrow(() -> new EntityNotFoundException("StatutTitreType introuvable: " + idStatutType));
        StatutCarteResident statut = new StatutCarteResident();
        statut.setCarteResident(carte);
        statut.setStatutType(type);
        statut.setDateChangement(LocalDateTime.now());
        statut.setCommentaire(commentaire);
        statutCarteResidentRepository.save(statut);
    }

    public Map<String, String> validerRecherche(String nom, String prenom, String numeroPasseport) {
        Map<String, String> e = new HashMap<>();
        if (isBlank(nom)) {
            e.put("nom", "Nom obligatoire.");
        }
        if (isBlank(prenom)) {
            e.put("prenom", "Prenom obligatoire.");
        }
        if (isBlank(numeroPasseport)) {
            e.put("numeroPasseport", "Numero de passeport obligatoire.");
        }
        return e;
    }

    public Map<String, String> validerDemandeurTrouve(
            LocalDate dateDemande,
            Boolean cocheVisa,
            Boolean cocheCarte,
            String referenceVisa,
            String referenceCarte) {
        Map<String, String> erreurs = new HashMap<>();

        if (dateDemande == null) {
            erreurs.put("dateDemande", "La date de la demande est obligatoire.");
        }
        boolean visaCoche = Boolean.TRUE.equals(cocheVisa);
        boolean carteCoche = Boolean.TRUE.equals(cocheCarte);
        if (!visaCoche && !carteCoche) {
            erreurs.put("titres", "Veuillez cocher au moins un titre a dupliquer.");
        }
        if (visaCoche && isBlank(referenceVisa)) {
            erreurs.put("referenceVisa", "La reference du visa est obligatoire.");
        }
        if (carteCoche && isBlank(referenceCarte)) {
            erreurs.put("referenceCarte", "La reference de la carte est obligatoire.");
        }

        return erreurs;
    }

    public Map<String, String> validerDemandeur(
            String nom,
            String prenom,
            LocalDate dateNaissance,
            String lieuNaissance,
            String telephone,
            String email,
            String adresse,
            Integer idNationalite,
            Integer idSituationFamiliale) {
        Map<String, String> e = new HashMap<>();
        if (isBlank(nom)) {
            e.put("nom", "Nom obligatoire.");
        }
        if (isBlank(prenom)) {
            e.put("prenom", "Prenom obligatoire.");
        }
        if (dateNaissance == null) {
            e.put("dateNaissance", "Date de naissance obligatoire.");
        }
        if (isBlank(lieuNaissance)) {
            e.put("lieuNaissance", "Lieu de naissance obligatoire.");
        }
        if (isBlank(telephone)) {
            e.put("telephone", "Telephone obligatoire.");
        }
        if (isBlank(email)) {
            e.put("email", "Email obligatoire.");
        }
        if (isBlank(adresse)) {
            e.put("adresse", "Adresse obligatoire.");
        }
        if (idNationalite == null) {
            e.put("idNationalite", "Nationalite obligatoire.");
        }
        if (idSituationFamiliale == null) {
            e.put("idSituationFamiliale", "Situation familiale obligatoire.");
        }
        return e;
    }

    public Map<String, String> validerEtape2(
            String numeroPasseport,
            LocalDate dateDelivrance,
            LocalDate dateExpiration,
            String paysDelivrance,
            String numeroReferenceVisa,
            LocalDate dateExpirationVisa) {
        Map<String, String> e = new HashMap<>();
        if (isBlank(numeroPasseport)) {
            e.put("numeroPasseport", "Numero passeport obligatoire.");
        }
        if (dateDelivrance == null) {
            e.put("dateDelivrance", "Date delivrance obligatoire.");
        }
        if (dateExpiration == null) {
            e.put("dateExpiration", "Date expiration obligatoire.");
        }
        if (dateDelivrance != null && dateExpiration != null && !dateExpiration.isAfter(dateDelivrance)) {
            e.put("dateExpiration", "Date expiration doit etre apres date delivrance.");
        }
        if (isBlank(paysDelivrance)) {
            e.put("paysDelivrance", "Pays de delivrance obligatoire.");
        }
        if (isBlank(numeroReferenceVisa)) {
            e.put("numeroReferenceVisa", "Numero de reference visa obligatoire.");
        }
        if (dateExpirationVisa == null) {
            e.put("dateExpirationVisa", "Date d'expiration du visa transformable obligatoire.");
        }
        return e;
    }

    public Map<String, String> validerEtape3a(
            Integer typeVisaId,
            LocalDate dateDemande,
            LocalDate dateExpirationVisa,
            Map<Integer, Boolean> piecesCommunes,
            List<TypePieceCommune> toutesLesPieces) {
        Map<String, String> e = new HashMap<>();
        if (typeVisaId == null) {
            e.put("typeVisaId", "Type visa obligatoire.");
        }
        if (dateDemande == null) {
            e.put("dateDemande", "Date demande obligatoire.");
        }
        if (!visaTransformableValide(dateExpirationVisa, dateDemande)) {
            e.put("dateDemande", "Le visa transformable est expire pour cette date de demande.");
        }
        e.putAll(validerPiecesCommunes(piecesCommunes, toutesLesPieces));
        return e;
    }

    public Map<String, String> validerPiecesSpecifiques(
            Map<Integer, Boolean> piecesSpecifiques,
            List<TypePieceSpecifique> toutesLesPieces) {
        Map<String, String> e = new HashMap<>();
        for (TypePieceSpecifique p : toutesLesPieces) {
            if (Boolean.TRUE.equals(p.getObligatoire()) && !Boolean.TRUE.equals(piecesSpecifiques.get(p.getId()))) {
                e.put("piecesSpecifiques", "Des pieces specifiques obligatoires sont manquantes.");
                break;
            }
        }
        return e;
    }

    public Map<String, String> validerInfosTitre(String reference, LocalDate dateDebut, LocalDate dateFin) {
        Map<String, String> e = new HashMap<>();
        if (isBlank(reference)) {
            e.put("reference", "Reference obligatoire.");
        }
        if (dateDebut == null) {
            e.put("dateDebut", "Date debut obligatoire.");
        }
        if (dateFin == null) {
            e.put("dateFin", "Date fin obligatoire.");
        }
        if (dateDebut != null && dateFin != null && !dateFin.isAfter(dateDebut)) {
            e.put("dateFin", "Date fin doit etre apres date debut.");
        }
        return e;
    }

    public Map<String, String> validerAncienneCarte(String reference, LocalDate dateDebut, LocalDate dateFin) {
        return validerInfosTitre(reference, dateDebut, dateFin);
    }

    @Transactional
    public Demande traiterDemandeurTrouve(
            Integer idDemandeur,
            Integer idPasseport,
            Integer typeDemandeId,
            LocalDate dateDemande,
            boolean cocheVisa,
            boolean cocheCarte,
            String referenceVisa,
            String referenceCarte) {
        Demandeur demandeur = findDemandeurById(idDemandeur);
        Passeport passeport = findPasseportById(idPasseport);
        TypeDemande typeDemande = typeDemandeService.findById(typeDemandeId);

        log.info("Demandeur trouve - demarrage duplicata demandeur={} passeport={} type={}",
                demandeur.getId(), passeport.getId(), typeDemande.getLibelle());

        Visa visaSource = null;
        Demande demandeVisa = null;
        if (cocheVisa) {
            visaSource = visaService.findByReference(referenceVisa)
                    .orElseThrow(() -> new EntityNotFoundException("Visa introuvable pour la reference: " + referenceVisa));
            if (visaSource.getPasseport() == null || !idPasseport.equals(visaSource.getPasseport().getId())) {
                throw new IllegalArgumentException("Le visa ne correspond pas au passeport du demandeur trouve.");
            }
            demandeVisa = demandeService.findByVisa(visaSource);
            log.info("Titre visa trouve - visa={} demandeOrigine={}", visaSource.getId(), demandeVisa.getId());
        }

        CarteResident carteSource = null;
        Demande demandeCarte = null;
        if (cocheCarte) {
            carteSource = carteResidentService.findByReference(referenceCarte)
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Carte de resident introuvable pour la reference: " + referenceCarte));
            if (carteSource.getPasseport() == null || !idPasseport.equals(carteSource.getPasseport().getId())) {
                throw new IllegalArgumentException("La carte de resident ne correspond pas au passeport du demandeur trouve.");
            }
            demandeCarte = demandeService.findByCarteResident(carteSource);
            log.info("Titre carte trouve - carte={} demandeOrigine={}", carteSource.getId(), demandeCarte.getId());
        }

        Demande demandeOrigine = demandeVisa != null ? demandeVisa : demandeCarte;
        if (demandeOrigine == null) {
            throw new IllegalStateException("Aucune demande d'origine n'a pu etre determinee.");
        }

        Demande nouvelleDemande = demandeService.creerDemandeDepuisOrigine(
                demandeOrigine,
                dateDemande,
                typeDemandeId,
                6);
        log.info("Nouvelle demande creee depuis origine - origine={} nouvelle={}",
                demandeOrigine.getId(), nouvelleDemande.getId());

        demandeLieeService.lier(demandeOrigine, nouvelleDemande, "duplicata");
        log.info("Lien de demande cree - origine={} liee={}", demandeOrigine.getId(), nouvelleDemande.getId());

        if (cocheVisa && visaSource != null) {
            visaService.changerStatut(visaSource, 3, "Visa declare perdu - duplicata demande");

            Visa nouveauVisa = new Visa();
            nouveauVisa.setDemande(nouvelleDemande);
            nouveauVisa.setReference(visaSource.getReference());
            nouveauVisa.setDateDebut(visaSource.getDateDebut());
            nouveauVisa.setDateFin(visaSource.getDateFin());
            nouveauVisa.setPasseport(visaSource.getPasseport());
            nouveauVisa = visaService.save(nouveauVisa);

            visaService.changerStatut(nouveauVisa, 1, null);
            log.info("Duplicata visa cree - source={} nouveau={}", visaSource.getId(), nouveauVisa.getId());
        }

        if (cocheCarte && carteSource != null) {
            carteResidentService.changerStatut(carteSource, 3, "Carte declaree perdue - duplicata demande");
            CarteResident nouvelleCarte = carteResidentService.dupliquerAvecNouvelledemande(carteSource, nouvelleDemande);
            carteResidentService.changerStatut(nouvelleCarte, 1, null);
            log.info("Duplicata carte cree - source={} nouveau={}", carteSource.getId(), nouvelleCarte.getId());
        }

        return nouvelleDemande;
    }

    @Transactional
    public Demande creerDemandeEnCoursDeSaisie(
            Demandeur demandeur,
            Map<String, Object> passeportData,
            Integer typeVisaId,
            LocalDate dateDemande,
            String numeroReferenceVisa,
            LocalDate dateExpirationVisa,
            Map<Integer, Boolean> piecesCommunes,
            Map<Integer, Boolean> piecesSpecifiques) {

        Demandeur savedDemandeur = demandeurRepository.save(demandeur);
        Passeport passeport = construirePasseport(savedDemandeur, passeportData);
        Passeport savedPasseport = passeportRepository.save(passeport);

        TypeVisa typeVisa = typeVisaRepository.findById(typeVisaId)
                .orElseThrow(() -> new EntityNotFoundException("TypeVisa introuvable: " + typeVisaId));
        TypeDemande typeDemande = typeDemandeRepository.findById(1)
                .orElseThrow(() -> new EntityNotFoundException("TypeDemande id=1 introuvable"));

        VisaTransformable visaTransformable = obtenirOuCreerVisaTransformable(
                numeroReferenceVisa,
                dateExpirationVisa,
                savedDemandeur,
                savedPasseport);

        Demande demande = new Demande();
        demande.setDemandeur(savedDemandeur);
        demande.setDateDemande(dateDemande);
        demande.setTypeVisa(typeVisa);
        demande.setTypeDemande(typeDemande);
        demande.setVisaTransformable(visaTransformable);
        Demande savedDemande = demandeRepository.save(demande);

        changerStatutDemande(savedDemande, 7);

        for (TypePieceCommune typePiece : typePieceCommuneRepository.findAll()) {
            PieceDemande piece = new PieceDemande();
            piece.setDemande(savedDemande);
            piece.setTypePieceCommune(typePiece);
            piece.setPresente(Boolean.TRUE.equals(piecesCommunes.get(typePiece.getId())));
            piece.setUploaded(false);
            pieceDemandeRepository.save(piece);
        }

        for (TypePieceSpecifique typePiece : typePieceSpecifiqueRepository.findByIdTypeVisa(typeVisaId)) {
            PieceDemandeSpecifique piece = new PieceDemandeSpecifique();
            piece.setDemande(savedDemande);
            piece.setTypePiece(typePiece);
            piece.setPresente(Boolean.TRUE.equals(piecesSpecifiques.get(typePiece.getId())));
            piece.setUploaded(false);
            pieceDemandeSpecifiqueRepository.save(piece);
        }

        return savedDemande;
    }

    @Transactional
    public CarteResident finaliserPhase1(
            Integer idDemande,
            Integer idPasseport,
            String reference,
            LocalDate dateDebut,
            LocalDate dateFin) {
        Demande demande = findDemandeById(idDemande);

        Optional<StatutDemande> dernierStatut = statutDemandeRepository.findFirstByDemande_IdOrderByDateChangementDesc(idDemande);
        if (dernierStatut.isEmpty() || dernierStatut.get().getStatutDemandeType() == null
                || !Integer.valueOf(7).equals(dernierStatut.get().getStatutDemandeType().getId())) {
            throw new IllegalStateException("La demande n'est pas en statut En cours de saisie.");
        }

        List<String> manquantes = getPiecesObligatoiresNonUploadees(demande);
        if (!manquantes.isEmpty()) {
            throw new IllegalStateException("Pieces obligatoires manquantes: " + String.join(", ", manquantes));
        }

        if (dateDebut == null || dateFin == null || !dateFin.isAfter(dateDebut)) {
            throw new IllegalArgumentException("La date de fin doit etre apres la date de debut.");
        }

        Passeport passeport = findPasseportById(idPasseport);

        CarteResident carte = new CarteResident();
        carte.setDemande(demande);
        carte.setPasseport(passeport);
        carte.setReference(reference);
        carte.setDateDebut(dateDebut);
        carte.setDateFin(dateFin);
        CarteResident saved = carteResidentRepository.save(carte);

        changerStatutCarte(saved, 1, "Titre actif apres finalisation de la phase 1");
        changerStatutDemande(demande, 5);

        log.info("Phase 1 finalisee - demande={}", demande.getId());
        return saved;
    }

    @Transactional
    public CarteResident enregistrerAncienneCarte(
            Integer idDemandePhase1,
            Integer idPasseport,
            String reference,
            LocalDate dateDebut,
            LocalDate dateFin) {
        if (dateDebut == null || dateFin == null || !dateFin.isAfter(dateDebut)) {
            throw new IllegalArgumentException("La date de fin doit etre apres la date de debut.");
        }

        Demande demandePhase1 = findDemandeById(idDemandePhase1);
        Passeport passeport = findPasseportById(idPasseport);

        CarteResident ancienneCarte = new CarteResident();
        ancienneCarte.setDemande(demandePhase1);
        ancienneCarte.setPasseport(passeport);
        ancienneCarte.setReference(reference);
        ancienneCarte.setDateDebut(dateDebut);
        ancienneCarte.setDateFin(dateFin);
        CarteResident saved = carteResidentRepository.save(ancienneCarte);

        Integer idPerdu = statutTitreTypeRepository.findByLibelle("Perdu")
                .map(StatutTitreType::getId)
                .orElse(5);
        changerStatutCarte(saved, idPerdu, "Carte declaree perdue - duplicata demande");

        log.info("Ancienne carte enregistree - demande={}", idDemandePhase1);
        return saved;
    }

    @Transactional
    public Demande finaliserPhase3(
            Integer idDemandePhase1,
            Integer idPasseport,
            Integer typeVisaId,
            LocalDate dateDemande) {
        Demande demandePhase1 = findDemandeById(idDemandePhase1);
        Passeport passeport = findPasseportById(idPasseport);

        TypeVisa typeVisa = typeVisaRepository.findById(typeVisaId)
                .orElseThrow(() -> new EntityNotFoundException("TypeVisa introuvable: " + typeVisaId));
        TypeDemande typeDemandeDuplicata = typeDemandeRepository.findById(2)
                .orElseThrow(() -> new EntityNotFoundException("TypeDemande id=2 introuvable"));

        Demande nouvelleDemande = new Demande();
        nouvelleDemande.setDemandeur(demandePhase1.getDemandeur());
        nouvelleDemande.setDateDemande(dateDemande == null ? LocalDate.now() : dateDemande);
        nouvelleDemande.setTypeVisa(typeVisa);
        nouvelleDemande.setTypeDemande(typeDemandeDuplicata);
        nouvelleDemande.setVisaTransformable(null);
        Demande savedDemandeDuplicata = demandeRepository.save(nouvelleDemande);
        changerStatutDemande(savedDemandeDuplicata, 5);

        DemandeLiee lien = new DemandeLiee();
        lien.setDemandeOrigine(demandePhase1);
        lien.setDemandeLiee(savedDemandeDuplicata);
        lien.setTypeLien("duplicata");
        demandeLieeRepository.save(lien);

        CarteResident carteSource = findCarteActiveParDemande(idDemandePhase1)
                .orElseThrow(() -> new IllegalStateException("Carte source phase 1 introuvable."));

        CarteResident nouvelleCarte = new CarteResident();
        nouvelleCarte.setDemande(savedDemandeDuplicata);
        nouvelleCarte.setPasseport(passeport);
        nouvelleCarte.setReference(carteSource.getReference());
        nouvelleCarte.setDateDebut(carteSource.getDateDebut());
        nouvelleCarte.setDateFin(carteSource.getDateFin());
        CarteResident savedCarte = carteResidentRepository.save(nouvelleCarte);
        changerStatutCarte(savedCarte, 1, "Duplicata actif");

        log.info("Phase 3 finalisee - duplicata={} origine={}", savedDemandeDuplicata.getId(), idDemandePhase1);
        return savedDemandeDuplicata;
    }

    public Passeport findPasseportByDemande(Demande demande) {
        if (demande.getVisaTransformable() == null || demande.getVisaTransformable().getPasseport() == null) {
            throw new EntityNotFoundException("Passeport introuvable pour demande " + demande.getId());
        }
        return demande.getVisaTransformable().getPasseport();
    }

    private Optional<CarteResident> findCarteActiveParDemande(Integer idDemande) {
        List<CarteResident> cartes = carteResidentRepository.findAll().stream()
                .filter(c -> c.getDemande() != null
                        && c.getDemande().getId() != null
                        && c.getDemande().getId().equals(idDemande))
                .toList();

        for (CarteResident carte : cartes) {
            Optional<StatutCarteResident> dernier =
                    statutCarteResidentRepository.findFirstByCarteResidentOrderByDateChangementDesc(carte);
            if (dernier.isPresent()
                    && dernier.get().getStatutType() != null
                    && "Actif".equalsIgnoreCase(dernier.get().getStatutType().getLibelle())) {
                return Optional.of(carte);
            }
        }

        return cartes.stream().findFirst();
    }

    private Map<String, String> validerPiecesCommunes(
            Map<Integer, Boolean> piecesCommunes,
            List<TypePieceCommune> toutesLesPieces) {
        Map<String, String> e = new HashMap<>();
        for (TypePieceCommune p : toutesLesPieces) {
            if (Boolean.TRUE.equals(p.getObligatoire()) && !Boolean.TRUE.equals(piecesCommunes.get(p.getId()))) {
                e.put("piecesCommunes", "Des pieces communes obligatoires sont manquantes.");
                break;
            }
        }
        return e;
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private static String extension(String fileName) {
        int i = fileName.lastIndexOf('.');
        if (i < 0 || i == fileName.length() - 1) {
            return "";
        }
        return fileName.substring(i + 1).toLowerCase(Locale.ROOT);
    }

    private static boolean isTypeAutorise(String ext) {
        return "jpg".equals(ext) || "jpeg".equals(ext) || "png".equals(ext) || "pdf".equals(ext);
    }
}
