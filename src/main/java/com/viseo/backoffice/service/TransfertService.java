package com.viseo.backoffice.service;

@Service
public class TransfertService {

    private static final Logger log = 
        LoggerFactory.getLogger(TransfertService.class);

    private final DemandeRepository demandeRepository;
    private final PasseportRepository passeportRepository;
    private final VisaRepository visaRepository;
    private final StatutVisaRepository statutVisaRepository;
    private final StatutTitreTypeRepository statutTitreTypeRepository;
    private final StatutDemandeRepository statutDemandeRepository;
    private final StatutDemandeTypeRepository statutDemandeTypeRepository;
    private final DemandeLieeRepository demandeLieeRepository;

    // constructeur avec tous les paramètres

    /**
     * Insère l'ancien visa avec statut "Passeport expire"
     * id_passeport = passeport inséré à l'étape 3b (premier passeport)
     * id_demande   = demande créée à l'étape 3b
     */
    @Transactional
    public Visa insererAncienVisa(
            Integer idDemande,
            Integer idPasseport,
            String reference,
            LocalDate dateDebut,
            LocalDate dateFin) {

        Demande demande = demandeRepository.findById(idDemande)
            .orElseThrow(() -> new EntityNotFoundException(
                "Demande non trouvée : " + idDemande));

        Passeport passeport = passeportRepository.findById(idPasseport)
            .orElseThrow(() -> new EntityNotFoundException(
                "Passeport non trouvé : " + idPasseport));

        // Créer l'ancien visa
        Visa ancienVisa = new Visa();
        ancienVisa.setDemande(demande);
        ancienVisa.setPasseport(passeport);
        ancienVisa.setReference(reference);
        ancienVisa.setDateDebut(dateDebut);
        ancienVisa.setDateFin(dateFin);
        Visa ancienVisaSauvegarde = visaRepository.save(ancienVisa);

        // Statut "Passeport expire"
        StatutTitreType statutPasseportExpire = statutTitreTypeRepository
            .findByLibelle("Passeport expire")
            .orElseThrow(() -> new EntityNotFoundException(
                "Statut Passeport expire non trouvé"));

        StatutVisa statutVisa = new StatutVisa();
        statutVisa.setVisa(ancienVisaSauvegarde);
        statutVisa.setStatutType(statutPasseportExpire);
        statutVisa.setDateChangement(LocalDateTime.now());
        statutVisa.setCommentaire(
            "Ancien visa — passeport expiré, transfert en cours");
        statutVisaRepository.save(statutVisa);

        log.info("Ancien visa inséré id={} statut=Passeport expire",
            ancienVisaSauvegarde.getId());

        return ancienVisaSauvegarde;
    }

    /**
     * Finalise le transfert :
     * 1. INSERT nouveau Passeport
     * 2. UPDATE statut demande origine → "Titre délivré"
     * 3. INSERT nouvelle Demande (type=transfert, statut="Dossier créé")
     * 4. INSERT DemandeLiee
     * 5. INSERT nouveau Visa (copie ancien, nouveau passeport + nouvelle demande)
     * 6. INSERT StatutVisa = "Actif"
     */
    @Transactional
    public Demande finaliserTransfert(
            Integer idDemandeOrigine,
            Visa ancienVisa,
            String numeroPasseport,
            LocalDate dateDelivrance,
            LocalDate dateExpiration,
            String paysDelivrance) {

        Demande demandeOrigine = demandeRepository.findById(idDemandeOrigine)
            .orElseThrow(() -> new EntityNotFoundException(
                "Demande origine non trouvée : " + idDemandeOrigine));

        // ÉTAPE 1 — INSERT nouveau Passeport
        Passeport nouveauPasseport = new Passeport();
        nouveauPasseport.setDemandeur(demandeOrigine.getDemandeur());
        nouveauPasseport.setNumeroPasseport(numeroPasseport);
        nouveauPasseport.setDateDelivrance(dateDelivrance);
        nouveauPasseport.setDateExpiration(dateExpiration);
        nouveauPasseport.setPaysDelivrance(paysDelivrance);
        Passeport nouveauPasseportSauvegarde =
            passeportRepository.save(nouveauPasseport);
        log.info("Nouveau passeport inséré id={}",
            nouveauPasseportSauvegarde.getId());

        // ÉTAPE 2 — UPDATE statut demande origine → "Titre délivré"
        StatutDemandeType titreDelivre = statutDemandeTypeRepository
            .findById(5)
            .orElseThrow(() -> new EntityNotFoundException(
                "Statut Titre délivré non trouvé"));
        StatutDemande statutOrigine = new StatutDemande();
        statutOrigine.setDemande(demandeOrigine);
        statutOrigine.setIdStatutType(titreDelivre);
        statutOrigine.setDateChangement(LocalDateTime.now());
        statutDemandeRepository.save(statutOrigine);
        log.info("Demande origine id={} → Titre délivré", idDemandeOrigine);

        // ÉTAPE 3 — INSERT nouvelle Demande (type=3 transfert)
        StatutDemandeType dossierCree = statutDemandeTypeRepository
            .findById(1)
            .orElseThrow(() -> new EntityNotFoundException(
                "Statut Dossier créé non trouvé"));

        Demande nouvelleDemande = new Demande();
        nouvelleDemande.setDemandeur(demandeOrigine.getDemandeur());
        nouvelleDemande.setIdTypeVisa(demandeOrigine.getIdTypeVisa());
        nouvelleDemande.setIdTypeDemande(
            typeDemandeRepository.findById(3)
                .orElseThrow(() -> new EntityNotFoundException(
                    "Type demande Transfert non trouvé")));
        nouvelleDemande.setDateDemande(demandeOrigine.getDateDemande());
        nouvelleDemande.setVisaTransformable(
            demandeOrigine.getVisaTransformable());
        Demande nouvelledemandeSauvegardee =
            demandeRepository.save(nouvelleDemande);

        StatutDemande statutNouvelleDemande = new StatutDemande();
        statutNouvelleDemande.setDemande(nouvelledemandeSauvegardee);
        statutNouvelleDemande.setIdStatutType(dossierCree);
        statutNouvelleDemande.setDateChangement(LocalDateTime.now());
        statutDemandeRepository.save(statutNouvelleDemande);
        log.info("Nouvelle demande transfert créée id={}",
            nouvelledemandeSauvegardee.getId());

        // ÉTAPE 4 — INSERT DemandeLiee
        DemandeLiee demandeLiee = new DemandeLiee();
        demandeLiee.setDemandeOrigine(demandeOrigine);
        demandeLiee.setDemandeLiee(nouvelledemandeSauvegardee);
        demandeLiee.setTypeLien("transfert");
        demandeLieeRepository.save(demandeLiee);
        log.info("DemandeLiee créée origine={} liee={} type=transfert",
            idDemandeOrigine, nouvelledemandeSauvegardee.getId());

        // ÉTAPE 5 — INSERT nouveau Visa (copie ancien)
        Visa nouveauVisa = new Visa();
        nouveauVisa.setDemande(nouvelledemandeSauvegardee);
        nouveauVisa.setPasseport(nouveauPasseportSauvegarde);
        nouveauVisa.setReference(ancienVisa.getReference());
        nouveauVisa.setDateDebut(ancienVisa.getDateDebut());
        nouveauVisa.setDateFin(ancienVisa.getDateFin());
        Visa nouveauVisaSauvegarde = visaRepository.save(nouveauVisa);

        // ÉTAPE 6 — INSERT StatutVisa = "Actif"
        StatutTitreType actif = statutTitreTypeRepository
            .findByLibelle("Actif")
            .orElseThrow(() -> new EntityNotFoundException(
                "Statut Actif non trouvé"));
        StatutVisa statutNouveauVisa = new StatutVisa();
        statutNouveauVisa.setVisa(nouveauVisaSauvegarde);
        statutNouveauVisa.setStatutType(actif);
        statutNouveauVisa.setDateChangement(LocalDateTime.now());
        statutNouveauVisa.setCommentaire("Nouveau visa — transfert effectué");
        statutVisaRepository.save(statutNouveauVisa);
        log.info("Nouveau visa créé id={} statut=Actif",
            nouveauVisaSauvegarde.getId());

        return nouvelledemandeSauvegardee;
    }

    /**
     * Valide les infos de l'ancien visa
     */
    public Map<String, String> validerAncienVisa(
            String reference,
            LocalDate dateDebut,
            LocalDate dateFin) {

        Map<String, String> erreurs = new LinkedHashMap<>();
        if (reference == null || reference.trim().isEmpty()) {
            erreurs.put("reference", "La référence est obligatoire.");
        }
        if (dateDebut == null) {
            erreurs.put("dateDebut", "La date de début est obligatoire.");
        }
        if (dateFin == null) {
            erreurs.put("dateFin", "La date de fin est obligatoire.");
        }
        if (dateDebut != null && dateFin != null
                && !dateDebut.isBefore(dateFin)) {
            erreurs.put("dateFin",
                "La date de fin doit être après la date de début.");
        }
        return erreurs;
    }

    /**
     * Valide les infos du nouveau passeport
     */
    public Map<String, String> validerNouveauPasseport(
            String numeroPasseport,
            LocalDate dateDelivrance,
            LocalDate dateExpiration,
            String paysDelivrance) {

        Map<String, String> erreurs = new LinkedHashMap<>();
        if (numeroPasseport == null || numeroPasseport.trim().isEmpty()) {
            erreurs.put("numeroPasseport",
                "Le numéro de passeport est obligatoire.");
        }
        if (dateDelivrance == null) {
            erreurs.put("dateDelivrance",
                "La date de délivrance est obligatoire.");
        }
        if (dateExpiration == null) {
            erreurs.put("dateExpiration",
                "La date d'expiration est obligatoire.");
        }
        if (dateDelivrance != null && dateExpiration != null
                && !dateDelivrance.isBefore(dateExpiration)) {
            erreurs.put("dateExpiration",
                "La date d'expiration doit être après la date de délivrance.");
        }
        if (paysDelivrance == null || paysDelivrance.trim().isEmpty()) {
            erreurs.put("paysDelivrance",
                "Le pays de délivrance est obligatoire.");
        }
        return erreurs;
    }
}