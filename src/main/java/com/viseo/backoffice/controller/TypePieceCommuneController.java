package com.viseo.backoffice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.viseo.backoffice.model.TypePieceCommune;
import com.viseo.backoffice.service.TypePieceCommuneService;

@Controller
@RequestMapping("/types-piece-commune")
public class TypePieceCommuneController {

    private final TypePieceCommuneService typePieceCommuneService;

    public TypePieceCommuneController(TypePieceCommuneService typePieceCommuneService) {
        this.typePieceCommuneService = typePieceCommuneService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("items", typePieceCommuneService.findAll());
        return "types-piece-commune/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("entity", new TypePieceCommune());
        return "types-piece-commune/form";
    }

    @PostMapping
    public String create(@ModelAttribute("entity") TypePieceCommune entity) {
        typePieceCommuneService.save(entity);
        return "redirect:/types-piece-commune";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        typePieceCommuneService.findById(id).ifPresent(entity -> model.addAttribute("entity", entity));
        return "types-piece-commune/detail";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id) {
        typePieceCommuneService.deleteById(id);
        return "redirect:/types-piece-commune";
    }
}
