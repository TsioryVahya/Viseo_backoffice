package com.viseo.backoffice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.viseo.backoffice.model.TypePieceSpecifique;
import com.viseo.backoffice.service.TypePieceSpecifiqueService;

@Controller
@RequestMapping("/types-piece-specifique")
public class TypePieceSpecifiqueController {

    private final TypePieceSpecifiqueService typePieceSpecifiqueService;

    public TypePieceSpecifiqueController(TypePieceSpecifiqueService typePieceSpecifiqueService) {
        this.typePieceSpecifiqueService = typePieceSpecifiqueService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("items", typePieceSpecifiqueService.findAll());
        return "types-piece-specifique/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("entity", new TypePieceSpecifique());
        return "types-piece-specifique/form";
    }

    @PostMapping
    public String create(@ModelAttribute("entity") TypePieceSpecifique entity) {
        typePieceSpecifiqueService.save(entity);
        return "redirect:/types-piece-specifique";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        typePieceSpecifiqueService.findById(id).ifPresent(entity -> model.addAttribute("entity", entity));
        return "types-piece-specifique/detail";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id) {
        typePieceSpecifiqueService.deleteById(id);
        return "redirect:/types-piece-specifique";
    }
}
