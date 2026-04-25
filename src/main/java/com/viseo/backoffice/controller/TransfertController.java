package com.viseo.backoffice.controller;

@Controller
public class TransfertController {

    private static final Logger log =
        LoggerFactory.getLogger(TransfertController.class);

    private final TransfertService transfertService;
    private final DuplicataService duplicataService;

    @Value("${upload.path}")
    private String uploadPath;

    // constructeur

    // ══════════════════════════════════════════
    // Les étapes 1 → etape4_upload
    // réutilisent les mêmes routes que duplicata
    // mais avec le prefix /transfert/
    // ══════════════════════════════════════════

    @GetMapping("/transfert/etape1")
    public String getEtape1(HttpSession session, Model model) {
        model.addAttribute("nationalites",
            duplicataService.findAllNationalites());
        model.addAttribute("situationsFamiliales",
            duplicataService.findAllSituationsFamiliales());
        model.addAttribute("demandeur",
            session.getAttribute("demandeur"));
        model.addAttribute("menuActif", "renouvellement");
        model.addAttribute("etapeActuelle", 1);
        return "transfert/etape1";
    }

    // POST etape1, GET/POST etape2, etape3a, etape3b
    // → même logique que DuplicataController
    // → appeler duplicataService pour les validations
    // → redirects vers /transfert/etapeX
    // → return "transfert/etapeX"

    @GetMapping("/transfert/etape4")
    public String getEtape4(HttpSession session, Model model) {
        Integer idDemande =
            (Integer) session.getAttribute("idDemandeNouveauTitre");
        Demande demande = duplicataService.findDemandeById(idDemande);
        // ... même logique que duplicata/etape4
        model.addAttribute("menuActif", "renouvellement");
        model.addAttribute("etapeActuelle", 4);
        return "transfert/etape4_upload";
    }

    @PostMapping("/transfert/etape4/piece")
    public String uploadPiece(
            @RequestParam Integer idPiece,
            @RequestParam String typePiece,
            @RequestParam MultipartFile fichier,
            @RequestParam @DateTimeFormat(iso = DATE) LocalDate dateUpload,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        Integer idDemande =
            (Integer) session.getAttribute("idDemandeNouveauTitre");
        try {
            duplicataService.traiterUpload(
                idDemande, idPiece, typePiece, fichier, dateUpload);
            redirectAttributes.addFlashAttribute("succes",
                "Fichier uploadé avec succès.");
        } catch (Exception e) {
            log.error("Erreur upload transfert pièce={} demande={}",
                idPiece, idDemande, e);
            redirectAttributes.addFlashAttribute("erreur",
                "Erreur upload : " + e.getMessage());
        }
        return "redirect:/transfert/etape4";
    }

    // ══════════════════════════════════════════
    // Étape 5 — Ancien visa
    // ══════════════════════════════════════════

    @GetMapping("/transfert/etape5")
    public String getEtape5(HttpSession session, Model model) {
        Integer idDemande =
            (Integer) session.getAttribute("idDemandeNouveauTitre");
        Demande demande = duplicataService.findDemandeById(idDemande);

        // Vérifier que toutes pièces obligatoires sont uploadées
        List<String> manquantes =
            duplicataService.getPiecesObligatoiresNonUploadees(demande);
        if (!manquantes.isEmpty()) {
            log.warn("Accès etape5 bloqué — manquantes={}", manquantes);
            redirectAttributes.addFlashAttribute("erreur",
                "Uploadez toutes les pièces obligatoires avant de continuer.");
            return "redirect:/transfert/etape4";
        }

        model.addAttribute("reference",
            session.getAttribute("referenceAncienVisa"));
        model.addAttribute("dateDebut",
            session.getAttribute("dateDebutAncienVisa"));
        model.addAttribute("dateFin",
            session.getAttribute("dateFinAncienVisa"));
        model.addAttribute("menuActif", "renouvellement");
        model.addAttribute("etapeActuelle", 5);
        return "transfert/etape5_ancien_visa";
    }

    @PostMapping("/transfert/etape5")
    public String traiterEtape5(
            @RequestParam String reference,
            @RequestParam @DateTimeFormat(iso = DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DATE) LocalDate dateFin,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

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
        return "redirect:/transfert/etape6";
    }

    // ══════════════════════════════════════════
    // Étape 6 — Nouveau passeport
    // ══════════════════════════════════════════

    @GetMapping("/transfert/etape6")
    public String getEtape6(HttpSession session, Model model) {
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

    @PostMapping("/transfert/etape6")
    public String traiterEtape6(
            @RequestParam String numeroPasseport,
            @RequestParam @DateTimeFormat(iso = DATE) LocalDate dateDelivrance,
            @RequestParam @DateTimeFormat(iso = DATE) LocalDate dateExpiration,
            @RequestParam String paysDelivrance,
            HttpSession session,
            Model model) {

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
        return "redirect:/transfert/confirmer";
    }

    // ══════════════════════════════════════════
    // Confirmation finale
    // ══════════════════════════════════════════

    @PostMapping("/transfert/confirmer")
    public String confirmer(
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        Integer idDemande =
            (Integer) session.getAttribute("idDemandeNouveauTitre");
        Integer idPasseport =
            (Integer) session.getAttribute("idPasseportSauvegarde");

        try {
            // ÉTAPE A — Insérer l'ancien visa
            Visa ancienVisa = transfertService.insererAncienVisa(
                idDemande,
                idPasseport,
                (String) session.getAttribute("referenceAncienVisa"),
                (LocalDate) session.getAttribute("dateDebutAncienVisa"),
                (LocalDate) session.getAttribute("dateFinAncienVisa"));

            // ÉTAPE B — Finaliser le transfert
            Demande nouvelleDemande = transfertService.finaliserTransfert(
                idDemande,
                ancienVisa,
                (String) session.getAttribute("nouveauNumeroPasseport"),
                (LocalDate) session.getAttribute("nouvelleDateDelivrance"),
                (LocalDate) session.getAttribute("nouvelleDateExpiration"),
                (String) session.getAttribute("nouveauPaysDelivrance"));

            log.info("Transfert finalisé — origine={} nouvelle={}",
                idDemande, nouvelleDemande.getId());
            session.invalidate();
            return "redirect:/demande/confirmation/" + nouvelleDemande.getId();

        } catch (Exception e) {
            log.error("Erreur confirmation transfert idDemande={}", idDemande, e);
            model.addAttribute("erreur",
                "Erreur lors de la confirmation : " + e.getMessage());
            return "transfert/etape6_nouveau_passeport";
        }
    }
}