package com.viseo.backoffice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.viseo.backoffice.model.PieceDemande;
import com.viseo.backoffice.service.PieceDemandeService;

@Controller
@RequestMapping("/pieces-demande")
public class PieceDemandeController {

    private final PieceDemandeService pieceDemandeService;

    public PieceDemandeController(PieceDemandeService pieceDemandeService) {
        this.pieceDemandeService = pieceDemandeService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("items", pieceDemandeService.findAll());
        return "pieces-demande/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("entity", new PieceDemande());
        return "pieces-demande/form";
    }

    @PostMapping
    public String create(@ModelAttribute("entity") PieceDemande entity) {
        pieceDemandeService.save(entity);
        return "redirect:/pieces-demande";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        pieceDemandeService.findById(id).ifPresent(entity -> model.addAttribute("entity", entity));
        return "pieces-demande/detail";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id) {
        pieceDemandeService.deleteById(id);
        return "redirect:/pieces-demande";
    }
}
