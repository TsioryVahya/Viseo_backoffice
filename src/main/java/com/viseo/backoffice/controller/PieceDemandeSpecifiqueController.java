package com.viseo.backoffice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.viseo.backoffice.model.PieceDemandeSpecifique;
import com.viseo.backoffice.service.PieceDemandeSpecifiqueService;

@Controller
@RequestMapping("/pieces-demande-specifique")
public class PieceDemandeSpecifiqueController {

    private final PieceDemandeSpecifiqueService pieceDemandeSpecifiqueService;

    public PieceDemandeSpecifiqueController(PieceDemandeSpecifiqueService pieceDemandeSpecifiqueService) {
        this.pieceDemandeSpecifiqueService = pieceDemandeSpecifiqueService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("items", pieceDemandeSpecifiqueService.findAll());
        return "pieces-demande-specifique/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("entity", new PieceDemandeSpecifique());
        return "pieces-demande-specifique/form";
    }

    @PostMapping
    public String create(@ModelAttribute("entity") PieceDemandeSpecifique entity) {
        pieceDemandeSpecifiqueService.save(entity);
        return "redirect:/pieces-demande-specifique";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        pieceDemandeSpecifiqueService.findById(id).ifPresent(entity -> model.addAttribute("entity", entity));
        return "pieces-demande-specifique/detail";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id) {
        pieceDemandeSpecifiqueService.deleteById(id);
        return "redirect:/pieces-demande-specifique";
    }
}
