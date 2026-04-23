package com.viseo.backoffice.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.viseo.backoffice.model.Demande;
import com.viseo.backoffice.model.PieceDemande;
import com.viseo.backoffice.model.PieceDemandeSpecifique;
import com.viseo.backoffice.model.UploadPiece;
import com.viseo.backoffice.service.ScanService;

@Controller
@RequestMapping("/scan")
public class ScanController {

    private static final Logger log = LoggerFactory.getLogger(ScanController.class);

    private final ScanService scanService;

    @Value("${upload.path}")
    private String uploadPath;

    public ScanController(ScanService scanService) {
        this.scanService = scanService;
    }

    @GetMapping("/liste")
    public String liste(
            @RequestParam(required = false) Integer statutId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        List<Integer> statutIds = statutId != null ? List.of(statutId) : List.of(1, 2);
        Pageable pageable = PageRequest.of(page, size);
        Page<Demande> resultat = scanService.findDemandesByDerniersStatuts(statutIds, pageable);

        model.addAttribute("pageDemandes", resultat);
        model.addAttribute("libellesDernierStatut", scanService.getLibellesDerniersStatuts(resultat.getContent()));
        model.addAttribute("statutFiltreActuel", statutId);
        model.addAttribute("menuActif", "scan");
        return "scan/liste";
    }

    @GetMapping("/upload/{idDemande}")
    public String upload(@PathVariable Integer idDemande, Model model) {
        Demande demande = scanService.findDemandeById(idDemande);
        List<PieceDemande> communes = scanService.findPiecesCommunesByDemande(demande);
        List<PieceDemandeSpecifique> specifiques = scanService.findPiecesSpecifiquesByDemande(demande);
        Map<Integer, UploadPiece> uploadsCommunes = scanService.getDerniersUploadsCommunes(communes);
        Map<Integer, UploadPiece> uploadsSpecifiques = scanService.getDerniersUploadsSpecifiques(specifiques);
        boolean peutTerminer = scanService.peutTerminerScan(demande);

        model.addAttribute("demande", demande);
        model.addAttribute("piecesCommunes", communes);
        model.addAttribute("piecesSpecifiques", specifiques);
        model.addAttribute("derniersUploadsCommunes", uploadsCommunes);
        model.addAttribute("derniersUploadsSpecifiques", uploadsSpecifiques);
        model.addAttribute("peutTerminer", peutTerminer);
        model.addAttribute("today", LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
        model.addAttribute("menuActif", "scan");
        return "scan/upload";
    }

    @PostMapping("/upload/{idDemande}/piece")
    public String uploadPiece(
            @PathVariable Integer idDemande,
            @RequestParam Integer idPiece,
            @RequestParam String typePiece,
            @RequestParam MultipartFile fichier,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateUpload,
            RedirectAttributes redirectAttributes) {
        try {
            Demande demande = scanService.findDemandeById(idDemande);
            scanService.traiterUpload(idDemande, idPiece, typePiece, fichier, dateUpload, uploadPath);
            scanService.evaluerStatutApresUpload(demande);
            redirectAttributes.addFlashAttribute("succes", "Fichier uploade avec succes.");
        } catch (Exception e) {
            log.error("Erreur upload piece id={} demande={}", idPiece, idDemande, e);
            redirectAttributes.addFlashAttribute("erreur", "Erreur lors de l'upload : " + e.getMessage());
        }
        return "redirect:/scan/upload/" + idDemande;
    }

    @PostMapping("/terminer/{idDemande}")
    public String terminer(
            @PathVariable Integer idDemande,
            RedirectAttributes redirectAttributes) {

        Demande demande = scanService.findDemandeById(idDemande);
        boolean termine = scanService.terminerScan(demande);

        if (termine) {
            log.info("Scan termine pour demande id={}", idDemande);
            redirectAttributes.addFlashAttribute(
                    "succes",
                    "Scan termine - Le dossier n" + idDemande + " est maintenant complet.");
            return "redirect:/scan/liste";
        }

        List<String> manquantes = scanService.getPiecesObligatoiresNonUploadees(demande);
        log.warn("Tentative scan termine echouee - demande={} pieces manquantes={}", idDemande, manquantes);
        redirectAttributes.addFlashAttribute(
                "erreur",
                "Scan impossible : " + manquantes.size() + " piece(s) obligatoire(s) non uploadee(s).");
        return "redirect:/scan/upload/" + idDemande;
    }
}
