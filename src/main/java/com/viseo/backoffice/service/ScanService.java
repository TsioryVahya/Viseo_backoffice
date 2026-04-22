package com.viseo.backoffice.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.viseo.backoffice.model.Demande;
import com.viseo.backoffice.model.PieceDemande;
import com.viseo.backoffice.model.PieceDemandeSpecifique;
import com.viseo.backoffice.model.StatutDemande;
import com.viseo.backoffice.model.StatutDemandeType;
import com.viseo.backoffice.model.UploadPiece;
import com.viseo.backoffice.repository.DemandeRepository;
import com.viseo.backoffice.repository.PieceDemandeRepository;
import com.viseo.backoffice.repository.PieceDemandeSpecifiqueRepository;
import com.viseo.backoffice.repository.StatutDemandeRepository;
import com.viseo.backoffice.repository.StatutDemandeTypeRepository;
import com.viseo.backoffice.repository.UploadPieceRepository;

@Service
public class ScanService {

    private final DemandeRepository demandeRepository;
    private final PieceDemandeRepository pieceDemandeRepository;
    private final PieceDemandeSpecifiqueRepository pieceDemandeSpecifiqueRepository;
    private final StatutDemandeRepository statutDemandeRepository;
    private final StatutDemandeTypeRepository statutDemandeTypeRepository;
    private final UploadPieceRepository uploadPieceRepository;

    public ScanService(
            DemandeRepository demandeRepository,
            PieceDemandeRepository pieceDemandeRepository,
            PieceDemandeSpecifiqueRepository pieceDemandeSpecifiqueRepository,
            StatutDemandeRepository statutDemandeRepository,
            StatutDemandeTypeRepository statutDemandeTypeRepository,
            UploadPieceRepository uploadPieceRepository) {
        this.demandeRepository = demandeRepository;
        this.pieceDemandeRepository = pieceDemandeRepository;
        this.pieceDemandeSpecifiqueRepository = pieceDemandeSpecifiqueRepository;
        this.statutDemandeRepository = statutDemandeRepository;
        this.statutDemandeTypeRepository = statutDemandeTypeRepository;
        this.uploadPieceRepository = uploadPieceRepository;
    }

    public Page<Demande> findDemandesByDerniersStatuts(List<Integer> statutIds, Pageable pageable) {
        return demandeRepository.findDemandesByDerniersStatuts(statutIds, pageable);
    }

    public Demande findDemandeById(Integer id) {
        return demandeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Demande introuvable: " + id));
    }

    public List<PieceDemande> findPiecesCommunesByDemande(Demande demande) {
        return pieceDemandeRepository.findByDemande(demande);
    }

    public List<PieceDemandeSpecifique> findPiecesSpecifiquesByDemande(Demande demande) {
        return pieceDemandeSpecifiqueRepository.findByDemande(demande);
    }

    public Map<Integer, UploadPiece> getDerniersUploadsCommunes(List<PieceDemande> pieces) {
        Map<Integer, UploadPiece> result = new HashMap<>();
        for (PieceDemande piece : pieces) {
            uploadPieceRepository.findFirstByPieceDemandeOrderByDateUploadDesc(piece)
                    .ifPresent(upload -> result.put(piece.getId(), upload));
        }
        return result;
    }

    public Map<Integer, UploadPiece> getDerniersUploadsSpecifiques(List<PieceDemandeSpecifique> pieces) {
        Map<Integer, UploadPiece> result = new HashMap<>();
        for (PieceDemandeSpecifique piece : pieces) {
            uploadPieceRepository.findFirstByPieceDemandeSpecifiqueOrderByDateUploadDesc(piece)
                    .ifPresent(upload -> result.put(piece.getId(), upload));
        }
        return result;
    }

    public Map<Integer, String> getLibellesDerniersStatuts(List<Demande> demandes) {
        Map<Integer, String> result = new HashMap<>();
        for (Demande demande : demandes) {
            statutDemandeRepository.findFirstByDemande_IdOrderByDateChangementDesc(demande.getId())
                    .ifPresent(statut -> result.put(
                            demande.getId(),
                            statut.getStatutDemandeType().getLibelle()));
        }
        return result;
    }

    public void traiterUpload(
            Integer idDemande,
            Integer idPiece,
            String typePiece,
            MultipartFile fichier,
            LocalDate dateUpload,
            String uploadPath) {

        if (fichier == null || fichier.isEmpty()) {
            throw new IllegalArgumentException("Le fichier est vide.");
        }

        String original = Optional.ofNullable(fichier.getOriginalFilename()).orElse("fichier");
        String safeName = Paths.get(original).getFileName().toString();
        String extension = getExtension(safeName);
        if (!isExtensionAutorisee(extension)) {
            throw new IllegalArgumentException("Type de fichier invalide. Autorises: jpg, jpeg, png, pdf.");
        }

        if (dateUpload == null) {
            throw new IllegalArgumentException("La date d'upload est obligatoire.");
        }

        String typeNormalise = Optional.ofNullable(typePiece).orElse("").trim().toLowerCase(Locale.ROOT);
        boolean commune = "commune".equals(typeNormalise);
        boolean specifique = "specifique".equals(typeNormalise);
        if (!commune && !specifique) {
            throw new IllegalArgumentException("Type de piece invalide: " + typePiece);
        }

        Path dossierDemande = Paths.get(uploadPath, String.valueOf(idDemande));
        try {
            Files.createDirectories(dossierDemande);
        } catch (IOException e) {
            throw new RuntimeException("Impossible de creer le dossier d'upload.", e);
        }

        UploadPiece uploadPiece = new UploadPiece();
        String nomFinal;

        if (commune) {
            PieceDemande pieceDemande = pieceDemandeRepository.findById(idPiece)
                    .orElseThrow(() -> new IllegalArgumentException("Piece commune introuvable: " + idPiece));
            if (!pieceDemande.getDemande().getId().equals(idDemande)) {
                throw new IllegalArgumentException("La piece commune n'appartient pas a la demande.");
            }
            nomFinal = "commune_" + idPiece + "_" + safeName;
            pieceDemande.setUploaded(true);
            pieceDemandeRepository.save(pieceDemande);
            uploadPiece.setPieceDemande(pieceDemande);
        } else {
            PieceDemandeSpecifique pieceDemandeSpecifique = pieceDemandeSpecifiqueRepository.findById(idPiece)
                    .orElseThrow(() -> new IllegalArgumentException("Piece specifique introuvable: " + idPiece));
            if (!pieceDemandeSpecifique.getDemande().getId().equals(idDemande)) {
                throw new IllegalArgumentException("La piece specifique n'appartient pas a la demande.");
            }
            nomFinal = "specifique_" + idPiece + "_" + safeName;
            pieceDemandeSpecifique.setUploaded(true);
            pieceDemandeSpecifiqueRepository.save(pieceDemandeSpecifique);
            uploadPiece.setPieceDemandeSpecifique(pieceDemandeSpecifique);
        }

        Path destination = dossierDemande.resolve(nomFinal);
        try {
            Files.copy(fichier.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'ecriture du fichier sur disque.", e);
        }

        uploadPiece.setCheminFichier(String.valueOf(Paths.get(String.valueOf(idDemande), nomFinal)));
        uploadPiece.setNomFichierOriginal(safeName);
        uploadPiece.setDateUpload(dateUpload);
        uploadPieceRepository.save(uploadPiece);
    }

    public boolean toutesLesPiecesUploadees(Demande demande) {
        boolean communesOk = findPiecesCommunesByDemande(demande).stream()
                .allMatch(piece -> Boolean.TRUE.equals(piece.getUploaded()));
        boolean specifiquesOk = findPiecesSpecifiquesByDemande(demande).stream()
                .allMatch(piece -> Boolean.TRUE.equals(piece.getUploaded()));
        return communesOk && specifiquesOk;
    }

    public List<String> getPiecesObligatoiresNonUploadees(Demande demande) {
        List<String> manquantesCommunes = findPiecesCommunesByDemande(demande).stream()
                .filter(piece -> Boolean.TRUE.equals(piece.getTypePieceCommune().getObligatoire()))
                .filter(piece -> !Boolean.TRUE.equals(piece.getUploaded()))
                .map(piece -> piece.getTypePieceCommune().getLibelle())
                .toList();

        List<String> manquantesSpecifiques = findPiecesSpecifiquesByDemande(demande).stream()
                .filter(piece -> Boolean.TRUE.equals(piece.getTypePiece().getObligatoire()))
                .filter(piece -> !Boolean.TRUE.equals(piece.getUploaded()))
                .map(piece -> piece.getTypePiece().getLibelle())
                .toList();

        List<String> result = new java.util.ArrayList<>(manquantesCommunes);
        result.addAll(manquantesSpecifiques);
        return result;
    }

    public void changerStatut(Demande demande, Integer idStatutType) {
        StatutDemandeType statutType = statutDemandeTypeRepository.findById(idStatutType)
                .orElseThrow(() -> new IllegalArgumentException("Statut type introuvable: " + idStatutType));
        StatutDemande statutDemande = new StatutDemande();
        statutDemande.setDemande(demande);
        statutDemande.setStatutDemandeType(statutType);
        statutDemande.setDateChangement(LocalDateTime.now());
        statutDemandeRepository.save(statutDemande);
    }

    public void evaluerStatutApresUpload(Demande demande) {
        if (!toutesLesPiecesUploadees(demande)) {
            changerStatut(demande, 2);
        }
    }

    public boolean terminerScan(Demande demande) {
        if (getPiecesObligatoiresNonUploadees(demande).isEmpty()) {
            changerStatut(demande, 6);
            return true;
        }
        return false;
    }

    public boolean peutTerminerScan(Demande demande) {
        return getPiecesObligatoiresNonUploadees(demande).isEmpty();
    }

    private static String getExtension(String filename) {
        int idx = filename.lastIndexOf('.');
        if (idx < 0 || idx == filename.length() - 1) {
            return "";
        }
        return filename.substring(idx + 1).toLowerCase(Locale.ROOT);
    }

    private static boolean isExtensionAutorisee(String ext) {
        return "jpg".equals(ext) || "jpeg".equals(ext) || "png".equals(ext) || "pdf".equals(ext);
    }
}
