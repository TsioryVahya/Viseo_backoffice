package com.viseo.backoffice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.viseo.backoffice.model.TypeDemande;
import com.viseo.backoffice.service.TypeDemandeService;

@Controller
@RequestMapping("/types-demande")
public class TypeDemandeController {

    private final TypeDemandeService typeDemandeService;

    public TypeDemandeController(TypeDemandeService typeDemandeService) {
        this.typeDemandeService = typeDemandeService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("items", typeDemandeService.findAll());
        return "types-demande/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("entity", new TypeDemande());
        return "types-demande/form";
    }

    @PostMapping
    public String create(@ModelAttribute("entity") TypeDemande entity) {
        typeDemandeService.save(entity);
        return "redirect:/types-demande";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        typeDemandeService.findById(id).ifPresent(entity -> model.addAttribute("entity", entity));
        return "types-demande/detail";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id) {
        typeDemandeService.deleteById(id);
        return "redirect:/types-demande";
    }
}
