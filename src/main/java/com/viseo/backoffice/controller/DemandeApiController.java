package com.viseo.backoffice.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viseo.backoffice.service.DemandeService;
import com.viseo.backoffice.service.QRCodeService;

@RestController
@RequestMapping("/api/demandes")
@CrossOrigin(origins = "http://localhost:5173")
public class DemandeApiController {

    private final DemandeService demandeService;
    private final QRCodeService qrCodeService;

    public DemandeApiController(DemandeService demandeService, QRCodeService qrCodeService) {
        this.demandeService = demandeService;
        this.qrCodeService = qrCodeService;
    }

    @GetMapping
    public List<Map<String, Object>> getAllDemandes() {
        return demandeService.findAll().stream().map(d -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", d.getId());
            map.put("date_demande", d.getDateDemande());
            map.put("demandeur_nom", d.getDemandeur().getNom());
            map.put("demandeur_prenom", d.getDemandeur().getPrenom());
            map.put("type_visa", d.getTypeVisa().getLibelle());
            map.put("type_demande", d.getTypeDemande().getLibelle());
            map.put("statut", "Validee");
            return map;
        }).collect(Collectors.toList());
    }

    @GetMapping("/{id}/qr")
    public Map<String, String> getQRCode(@PathVariable Integer id) {
        Map<String, String> response = new HashMap<>();
        try {
            String link = "http://localhost:5173/demande/" + id;
            String qrBase64 = qrCodeService.generateQRCodeBase64(link, 300, 300);
            response.put("qr", "data:image/png;base64," + qrBase64);
            response.put("link", link);
        } catch (Exception e) {
            response.put("error", "Could not generate QR code: " + e.getMessage());
        }
        return response;
    }

    @GetMapping("/{id}")
    public Map<String, Object> getDemandeDetail(@PathVariable Integer id) {
        return demandeService.findOptionalById(id).map(d -> {
            Map<String, Object> detail = new HashMap<>();
            detail.put("id", d.getId());
            detail.put("date_demande", d.getDateDemande());
            
            Map<String, String> demandeur = new HashMap<>();
            demandeur.put("nom", d.getDemandeur().getNom());
            demandeur.put("prenom", d.getDemandeur().getPrenom());
            demandeur.put("email", d.getDemandeur().getEmail());
            demandeur.put("telephone", d.getDemandeur().getTelephone());
            
            detail.put("demandeur", demandeur);
            detail.put("type_visa", d.getTypeVisa().getLibelle());
            detail.put("type_demande", d.getTypeDemande().getLibelle());
            return detail;
        }).orElseGet(() -> {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Demande not found");
            return error;
        });
    }
}
