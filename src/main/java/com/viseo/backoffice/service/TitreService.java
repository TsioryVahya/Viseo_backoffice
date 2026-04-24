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
import java.util.LinkedHashMap;
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
import com.viseo.backoffice.model.UploadPiece;
import com.viseo.backoffice.model.Visa;
import com.viseo.backoffice.model.VisaTransformable;
import com.viseo.backoffice.repository.CarteResidentRepository;
import com.viseo.backoffice.repository.DemandeRepository;
import com.viseo.backoffice.repository.DemandeurRepository;
import com.viseo.backoffice.repository.NationaliteRepository;
import com.viseo.backoffice.repository.PasseportRepository;
import com.viseo.backoffice.repository.PieceDemandeRepository;
import com.viseo.backoffice.repository.PieceDemandeSpecifiqueRepository;
import com.viseo.backoffice.repository.SituationFamilialeRepository;
import com.viseo.backoffice.repository.StatutDemandeRepository;
import com.viseo.backoffice.repository.StatutDemandeTypeRepository;
import com.viseo.backoffice.repository.TypeDemandeRepository;
import com.viseo.backoffice.repository.TypePieceCommuneRepository;
import com.viseo.backoffice.repository.TypePieceSpecifiqueRepository;
import com.viseo.backoffice.repository.TypeVisaRepository;
import com.viseo.backoffice.repository.UploadPieceRepository;
import com.viseo.backoffice.repository.VisaRepository;
import com.viseo.backoffice.repository.VisaTransformableRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class TitreService {

    private static final Logger log = LoggerFactory.getLogger(TitreService.class);

    private final DemandeurRepository demandeurRepository;
    private final PasseportRepository passeportRepository;
    private final DemandeRepository demandeRepository;
    private final StatutDemandeRepository statutDemandeRepository;
    private final StatutDemandeTypeRepository statutDemandeTypeRepository;
    private final PieceDemandeRepository pieceDemandeRepository;
    private final PieceDemandeSpecifiqueRepository pieceDemandeSpecifiqueRepository;
    private final TypePieceCommuneRepository typePieceCommuneRepository;
    private final TypePieceSpecifiqueRepository typePieceSpecifiqueRepository;
    private final VisaRepository visaRepository;
    private final CarteResidentRepository carteResidentRepository;
    private final VisaTransformableRepository visaTransformableRepository;
    private final UploadPieceRepository uploadPieceRepository;
    private final TypeVisaRepository typeVisaRepository;
    private final TypeDemandeRepository typeDemandeRepository;
    private final NationaliteRepository nationaliteRepository;
    private final SituationFamilialeRepository situationFamilialeRepository;

    @Value("${upload.path}")
    private String uploadPath;

    public TitreService(
            DemandeurRepository demandeurRepository,
            PasseportRepository passeportRepository,
            DemandeRepository demandeRepository,
            StatutDemandeRepository statutDemandeRepository,
            StatutDemandeTypeRepository statutDemandeTypeRepository,
            PieceDemandeRepository pieceDemandeRepository,
            PieceDemandeSpecifiqueRepository pieceDemandeSpecifiqueRepository,
            TypePieceCommuneRepository typePieceCommuneRepository,
            TypePieceSpecifiqueRepository typePieceSpecifiqueRepository,
            VisaRepository visaRepository,
            CarteResidentRepository carteResidentRepository,
            VisaTransformableRepository visaTransformableRepository,
            UploadPieceRepository uploadPieceRepository,
            TypeVisaRepository typeVisaRepository,
            TypeDemandeRepository typeDemandeRepository,
            NationaliteRepository nationaliteRepository,
            SituationFamilialeRepository situationFamilialeRepository) {
        this.demandeurRepository = demandeurRepository;
        this.passeportRepository = passeportRepository;
        this.demandeRepository = demandeRepository;
        this.statutDemandeRepository = statutDemandeRepository;
        this.statutDemandeTypeRepository = statutDemandeTypeRepository;
        this.pieceDemandeRepository = pieceDemandeRepository;
        this.pieceDemandeSpecifiqueRepository = pieceDemandeSpecifiqueRepository;
        this.typePieceCommuneRepository = typePieceCommuneRepository;
        this.typePieceSpecifiqueRepository = typePieceSpecifiqueRepository;
        this.visaRepository = visaRepository;
        this.carteResidentRepository = carteResidentRepository;
        this.visaTransformableRepository = visaTransformableRepository;
        this.uploadPieceRepository = uploadPieceRepository;
        this.typeVisaRepository = typeVisaRepository;
        this.typeDemandeRepository = typeDemandeRepository;
        this.nationaliteRepository = nationaliteRepository;
        this.situationFamilialeRepository = situationFamilialeRepository;
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

    public CarteResident findCarteResidentById(Integer id) {
        return carteResidentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CarteResident introuvable: " + id));
    }

    public Demande findDemandeById(Integer id) {
        return demandeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Demande introuvable: " + id));
    }

    public String findTypeVisaLibelleById(Integer id) {
        if (id == null) {
            return "";
        }
        return typeVisaRepository.findById(id)
                .map(TypeVisa::getLibelle)
                .orElse("");
    }

    public Optional<CarteResident> findDerniereCarteResident(Demandeur demandeur) {
        return carteResidentRepository.findFirstByPasseport_DemandeurOrderByDateFinDesc(demandeur);
    }

    public Optional<Visa> findDernierVisa(Demandeur demandeur) {
        return visaRepository.findFirstByPasseport_DemandeurOrderByDateFinDesc(demandeur);
    }

    public boolean visaTransformableValide(LocalDate dateExpiration, LocalDate dateDemande) {
        if (dateExpiration == null || dateDemande == null) {
            return false;
        }
        return dateExpiration.isAfter(dateDemande);
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

    @Transactional
    public Demande creerDemandeEnCoursDeSaisie(
            Demandeur demandeur,
            Passeport passeport,
            Integer typeVisaId,
            Integer typeDemandeId,
            LocalDate dateDemande,
            String numeroReferenceVisa,
            LocalDate dateExpirationVisa,
            Map<Integer, Boolean> piecesCommunes,
            Map<Integer, Boolean> piecesSpecifiques) {

        Demandeur savedDemandeur = demandeurRepository.save(demandeur);
        passeport.setDemandeur(savedDemandeur);
        Passeport savedPasseport = passeportRepository.save(passeport);

        TypeVisa typeVisa = typeVisaRepository.findById(typeVisaId)
                .orElseThrow(() -> new EntityNotFoundException("TypeVisa introuvable: " + typeVisaId));
        TypeDemande typeDemande = typeDemandeRepository.findById(typeDemandeId)
                .orElseThrow(() -> new EntityNotFoundException("TypeDemande introuvable: " + typeDemandeId));

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

        changerStatut(savedDemande, "En cours de saisie");

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

    public List<PieceDemande> findPiecesCommunesByDemande(Demande demande) {
        return pieceDemandeRepository.findByDemande(demande);
    }

    public List<PieceDemandeSpecifique> findPiecesSpecifiquesByDemande(Demande demande) {
        return pieceDemandeSpecifiqueRepository.findByDemande(demande);
    }

    public Map<Integer, UploadPiece> getDerniersUploadsCommunes(List<PieceDemande> pieces) {
        Map<Integer, UploadPiece> map = new HashMap<>();
        for (PieceDemande p : pieces) {
            uploadPieceRepository.findFirstByPieceDemandeOrderByDateUploadDesc(p)
                    .ifPresent(upload -> map.put(p.getId(), upload));
        }
        return map;
    }

    public Map<Integer, UploadPiece> getDerniersUploadsSpecifiques(List<PieceDemandeSpecifique> pieces) {
        Map<Integer, UploadPiece> map = new HashMap<>();
        for (PieceDemandeSpecifique p : pieces) {
            uploadPieceRepository.findFirstByPieceDemandeSpecifiqueOrderByDateUploadDesc(p)
                    .ifPresent(upload -> map.put(p.getId(), upload));
        }
        return map;
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
        String nomOriginal = Optional.ofNullable(fichier.getOriginalFilename()).orElse("fichier");
        String nomSafe = Paths.get(nomOriginal).getFileName().toString();
        String ext = extension(nomSafe);
        if (!isTypeAutorise(ext)) {
            throw new IllegalArgumentException("Type fichier invalide. Autorises: jpg, jpeg, png, pdf.");
        }

        Path dossier = Paths.get(uploadPath, String.valueOf(idDemande));
        try {
            Files.createDirectories(dossier);
        } catch (IOException e) {
            throw new RuntimeException("Impossible de creer dossier upload.", e);
        }

        boolean commune = "commune".equalsIgnoreCase(typePiece);
        boolean specifique = "specifique".equalsIgnoreCase(typePiece);
        if (!commune && !specifique) {
            throw new IllegalArgumentException("typePiece invalide: " + typePiece);
        }

        UploadPiece up = new UploadPiece();
        String nomFichier = typePiece.toLowerCase(Locale.ROOT) + "_" + idPiece + "_" + nomSafe;

        if (commune) {
            PieceDemande piece = pieceDemandeRepository.findById(idPiece)
                    .orElseThrow(() -> new EntityNotFoundException("PieceDemande introuvable: " + idPiece));
            piece.setUploaded(true);
            pieceDemandeRepository.save(piece);
            up.setPieceDemande(piece);
        } else {
            PieceDemandeSpecifique piece = pieceDemandeSpecifiqueRepository.findById(idPiece)
                    .orElseThrow(() -> new EntityNotFoundException("PieceDemandeSpecifique introuvable: " + idPiece));
            piece.setUploaded(true);
            pieceDemandeSpecifiqueRepository.save(piece);
            up.setPieceDemandeSpecifique(piece);
        }

        Path cible = dossier.resolve(nomFichier);
        try {
            Files.copy(fichier.getInputStream(), cible, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Erreur ecriture fichier upload.", e);
        }

        up.setNomFichierOriginal(nomSafe);
        up.setCheminFichier(String.valueOf(Paths.get(String.valueOf(idDemande), nomFichier)));
        up.setDateUpload(dateUpload);
        uploadPieceRepository.save(up);
    }

    public boolean peutContinuerApresUpload(Demande demande) {
        return getPiecesObligatoiresNonUploadees(demande).isEmpty();
    }

    public List<String> getPiecesObligatoiresNonUploadees(Demande demande) {
        List<String> manquantes = new ArrayList<>();
        for (PieceDemande p : findPiecesCommunesByDemande(demande)) {
            if (Boolean.TRUE.equals(p.getTypePieceCommune().getObligatoire()) && !Boolean.TRUE.equals(p.getUploaded())) {
                manquantes.add(p.getTypePieceCommune().getLibelle());
            }
        }
        for (PieceDemandeSpecifique p : findPiecesSpecifiquesByDemande(demande)) {
            if (Boolean.TRUE.equals(p.getTypePiece().getObligatoire()) && !Boolean.TRUE.equals(p.getUploaded())) {
                manquantes.add(p.getTypePiece().getLibelle());
            }
        }
        return manquantes;
    }

    public void changerStatut(Demande demande, String libelleStatut) {
        StatutDemandeType type = statutDemandeTypeRepository.findByLibelle(libelleStatut)
                .orElseThrow(() -> new EntityNotFoundException("StatutDemandeType introuvable: " + libelleStatut));
        StatutDemande statut = new StatutDemande();
        statut.setDemande(demande);
        statut.setStatutDemandeType(type);
        statut.setDateChangement(LocalDateTime.now());
        statutDemandeRepository.save(statut);
    }

    @Transactional
    public Demande finaliserDuplicataTrouve(
            Demandeur demandeur,
            Passeport passeport,
            CarteResident ancienneCarteResident,
            String reference,
            LocalDate dateDebut,
            LocalDate dateFin) {
        if (dateDebut == null || dateFin == null || !dateFin.isAfter(dateDebut)) {
            throw new IllegalArgumentException("La date de fin doit etre apres la date de debut.");
        }
        Demande d = new Demande();
        d.setDemandeur(demandeur);
        d.setDateDemande(LocalDate.now());
        d.setTypeVisa(ancienneCarteResident.getDemande().getTypeVisa());
        d.setTypeDemande(typeDemandeRepository.findById(2)
                .orElseThrow(() -> new EntityNotFoundException("TypeDemande id=2 introuvable")));
        d.setVisaTransformable(ancienneCarteResident.getDemande().getVisaTransformable());
        Demande saved = demandeRepository.save(d);

        CarteResident carte = new CarteResident();
        carte.setDemande(saved);
        carte.setPasseport(passeport);
        carte.setReference(reference);
        carte.setDateDebut(dateDebut);
        carte.setDateFin(dateFin);
        carteResidentRepository.save(carte);

        changerStatut(saved, "Titre delivre");
        return saved;
    }

    @Transactional
    public Demande finaliserTransfertTrouve(
            Demandeur demandeur,
            Passeport nouveauPasseport,
            Integer typeVisaId,
            String reference,
            LocalDate dateDebut,
            LocalDate dateFin) {
        if (dateDebut == null || dateFin == null || !dateFin.isAfter(dateDebut)) {
            throw new IllegalArgumentException("La date de fin doit etre apres la date de debut.");
        }

        Passeport savedPasseport = passeportRepository.save(nouveauPasseport);

        Demande d = new Demande();
        d.setDemandeur(demandeur);
        d.setDateDemande(LocalDate.now());
        d.setTypeVisa(typeVisaRepository.findById(typeVisaId)
                .orElseThrow(() -> new EntityNotFoundException("TypeVisa introuvable: " + typeVisaId)));
        d.setTypeDemande(typeDemandeRepository.findById(3)
                .orElseThrow(() -> new EntityNotFoundException("TypeDemande id=3 introuvable")));
        d.setVisaTransformable(null);
        Demande saved = demandeRepository.save(d);

        Visa visa = new Visa();
        visa.setDemande(saved);
        visa.setPasseport(savedPasseport);
        visa.setReference(reference);
        visa.setDateDebut(dateDebut);
        visa.setDateFin(dateFin);
        visaRepository.save(visa);

        changerStatut(saved, "Titre delivre");
        return saved;
    }

    @Transactional
    public Demande finaliserDemandeNonTrouve(
            Integer idDemande,
            String typeDemande,
            Integer idPasseport,
            String reference,
            LocalDate dateDebut,
            LocalDate dateFin) {
        Demande demande = findDemandeById(idDemande);

        Optional<StatutDemande> dernierStatut = statutDemandeRepository.findFirstByDemande_IdOrderByDateChangementDesc(idDemande);
        if (dernierStatut.isEmpty() || !"En cours de saisie".equalsIgnoreCase(dernierStatut.get().getStatutDemandeType().getLibelle())) {
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
        if ("duplicata".equalsIgnoreCase(typeDemande)) {
            CarteResident cr = new CarteResident();
            cr.setDemande(demande);
            cr.setPasseport(passeport);
            cr.setReference(reference);
            cr.setDateDebut(dateDebut);
            cr.setDateFin(dateFin);
            carteResidentRepository.save(cr);
        } else if ("transfert".equalsIgnoreCase(typeDemande)) {
            Visa visa = new Visa();
            visa.setDemande(demande);
            visa.setPasseport(passeport);
            visa.setReference(reference);
            visa.setDateDebut(dateDebut);
            visa.setDateFin(dateFin);
            visaRepository.save(visa);
        } else {
            throw new IllegalArgumentException("Type de demande invalide: " + typeDemande);
        }

        changerStatut(demande, "Titre delivre");
        return demande;
    }

    public Map<String, String> validerRecherche(String nom, String prenom, String numeroPasseport) {
        Map<String, String> e = new HashMap<>();
        if (isBlank(nom)) {
            e.put("nom", "Le nom est obligatoire.");
        }
        if (isBlank(prenom)) {
            e.put("prenom", "Le prenom est obligatoire.");
        }
        if (isBlank(numeroPasseport)) {
            e.put("numeroPasseport", "Le numero de passeport est obligatoire.");
        }
        return e;
    }

    public Map<String, String> validerPasseport(
            String numeroPasseport,
            LocalDate dateDelivrance,
            LocalDate dateExpiration,
            String paysDelivrance) {
        Map<String, String> e = new HashMap<>();
        if (isBlank(numeroPasseport)) {
            e.put("numeroPasseport", "Numero passeport obligatoire.");
        }
        if (dateDelivrance == null) {
            e.put("dateDelivrance", "Date de delivrance obligatoire.");
        }
        if (dateExpiration == null) {
            e.put("dateExpiration", "Date d'expiration obligatoire.");
        }
        if (dateDelivrance != null && dateExpiration != null && !dateExpiration.isAfter(dateDelivrance)) {
            e.put("dateExpiration", "Date expiration doit etre apres delivrance.");
        }
        if (isBlank(paysDelivrance)) {
            e.put("paysDelivrance", "Pays de delivrance obligatoire.");
        }
        return e;
    }

    public Map<String, String> validerTitre(String reference, LocalDate dateDebut, LocalDate dateFin) {
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

    public Map<String, String> validerPiecesCommunes(
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

    public Map<String, Object> traiterRecherche(String nom, String prenom, String numeroPasseport, String type) {
        Map<String, Object> resultat = new HashMap<>();
        Optional<Demandeur> demandeurOpt = rechercherDemandeur(nom, prenom, numeroPasseport);
        if (demandeurOpt.isEmpty()) {
            resultat.put("demandeurTrouve", false);
            resultat.put("demandeur", null);
            resultat.put("passeport", null);
            resultat.put("carteResident", null);
            resultat.put("erreurMetier", null);
            return resultat;
        }

        Demandeur demandeur = demandeurOpt.get();
        Optional<Passeport> passeport = findPasseportByNumero(numeroPasseport);
        if (passeport.isEmpty()) {
            resultat.put("demandeurTrouve", false);
            resultat.put("demandeur", null);
            resultat.put("passeport", null);
            resultat.put("carteResident", null);
            resultat.put("erreurMetier", null);
            return resultat;
        }

        resultat.put("demandeurTrouve", true);
        resultat.put("demandeur", demandeur);
        resultat.put("passeport", passeport.get());
        resultat.put("erreurMetier", null);

        if ("duplicata".equalsIgnoreCase(type)) {
            Optional<CarteResident> carte = findDerniereCarteResident(demandeur);
            if (carte.isEmpty()) {
                resultat.put("carteResident", null);
                resultat.put("erreurMetier", "Aucune carte de resident precedente trouvee.");
            } else {
                resultat.put("carteResident", carte.get());
            }
        } else {
            resultat.put("carteResident", null);
            Optional<Visa> dernierVisa = findDernierVisa(demandeur);
            if (dernierVisa.isPresent() && dernierVisa.get().getDemande() != null && dernierVisa.get().getDemande().getTypeVisa() != null) {
                resultat.put("typeVisaId", dernierVisa.get().getDemande().getTypeVisa().getId());
            }
        }
        return resultat;
    }

    public Passeport construireNouveauPasseport(
            Demandeur demandeur,
            String numero,
            LocalDate delivrance,
            LocalDate expiration,
            String pays) {
        Passeport p = new Passeport();
        p.setDemandeur(demandeur);
        p.setNumeroPasseport(numero);
        p.setDateDelivrance(delivrance);
        p.setDateExpiration(expiration);
        p.setPaysDelivrance(pays);
        return p;
    }

    public List<Nationalite> findAllNationalites() {
        return nationaliteRepository.findAll();
    }

    public List<SituationFamiliale> findAllSituationsFamiliales() {
        return situationFamilialeRepository.findAll();
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

    public Map<String, String> validerEtape2(
            String numeroPasseport,
            LocalDate dateDelivrance,
            LocalDate dateExpiration,
            String paysDelivrance,
            String numeroReferenceVisa,
            LocalDate dateExpirationVisa) {
        Map<String, String> e = validerPasseport(numeroPasseport, dateDelivrance, dateExpiration, paysDelivrance);
        if (isBlank(numeroReferenceVisa)) {
            e.put("numeroReferenceVisa", "Numero reference visa obligatoire.");
        }
        if (dateExpirationVisa == null) {
            e.put("dateExpirationVisa", "Date expiration visa obligatoire.");
        }
        return e;
    }

    public List<TypeVisa> findAllTypesVisa() {
        return typeVisaRepository.findAll();
    }

    public List<TypePieceCommune> findAllPiecesCommunes() {
        return typePieceCommuneRepository.findAll();
    }

    public Map<String, String> validerEtape3a(
            String typeDemande,
            Integer typeVisaId,
            LocalDate dateDemande,
            LocalDate dateExpirationVisa,
            Map<Integer, Boolean> piecesCommunes,
            List<TypePieceCommune> toutesLesPieces) {
        Map<String, String> e = new HashMap<>();
        if ("transfert".equalsIgnoreCase(typeDemande) && typeVisaId == null) {
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

    public List<TypePieceSpecifique> findPiecesSpecifiquesByTypeVisa(Integer typeVisaId) {
        if (typeVisaId == null) {
            return List.of();
        }
        return typePieceSpecifiqueRepository.findByIdTypeVisa(typeVisaId);
    }

    public Passeport findPasseportByDemande(Demande demande) {
        List<Passeport> passeports = passeportRepository.findByDemandeur(demande.getDemandeur());
        return passeports.stream()
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Passeport introuvable pour demande " + demande.getId()));
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

    public Map<Integer, Boolean> extractPiecesFromParams(Map<String, String> params, String prefix) {
        Map<Integer, Boolean> result = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (entry.getKey().startsWith(prefix)) {
                String idString = entry.getKey().substring(prefix.length());
                try {
                    result.put(Integer.parseInt(idString), true);
                } catch (NumberFormatException ignored) {
                    log.debug("Ignorer checkbox invalide key={}", entry.getKey());
                }
            }
        }
        return result;
    }
}
