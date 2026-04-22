package com.viseo.backoffice.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.viseo.backoffice.service.DemandeService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/demande")
public class DemandeNouveauTitreController {

    private final DemandeService demandeService;

    public DemandeNouveauTitreController(DemandeService demandeService) {
        this.demandeService = demandeService;
    }

    @GetMapping("/nouveau/etape1")
    public String afficherEtape1(Model model, HttpSession session) {
        return demandeService.afficherEtape1(model, session);
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
        return demandeService.traiterEtape1(
                nom,
                prenom,
                dateNaissance,
                lieuNaissance,
                telephone,
                email,
                adresse,
                idNationalite,
                idSituationFamiliale,
                model,
                session);
    }

    @GetMapping("/nouveau/etape2")
    public String afficherEtape2(Model model, HttpSession session) {
        return demandeService.afficherEtape2(model, session);
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
        return demandeService.traiterEtape2(
                numeroPasseport,
                dateDelivrance,
                dateExpiration,
                paysDelivrance,
                numeroReferenceVisa,
                dateExpirationVisa,
                model,
                session);
    }

    @GetMapping("/nouveau/etape3a")
    public String afficherEtape3a(Model model, HttpSession session) {
        return demandeService.afficherEtape3a(model, session);
    }

    @PostMapping("/nouveau/etape3a")
    public String traiterEtape3a(
            @RequestParam(required = false) String typeVisaId,
            @RequestParam(required = false) String dateDemande,
            @RequestParam Map<String, String> params,
            Model model,
            HttpSession session) {
        return demandeService.traiterEtape3a(typeVisaId, dateDemande, params, model, session);
    }

    @GetMapping("/nouveau/etape3b")
    public String afficherEtape3b(Model model, HttpSession session) {
        return demandeService.afficherEtape3b(model, session);
    }

    @PostMapping("/nouveau/etape3b")
    public String traiterEtape3b(
            @RequestParam Map<String, String> params,
            Model model,
            HttpSession session) {
        return demandeService.traiterEtape3b(params, session);
    }

    @GetMapping("/nouveau/etape4")
    public String afficherEtape4(Model model, HttpSession session) {
        return demandeService.afficherEtape4(model, session);
    }

    @PostMapping("/nouveau/etape4")
    public String traiterEtape4(Model model, HttpSession session) {
        return demandeService.traiterEtape4(model, session);
    }

    @GetMapping("/confirmation/{id}")
    public String confirmation(@PathVariable Integer id, Model model) {
        return demandeService.confirmation(id, model);
    }
}
