package com.viseo.backoffice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.viseo.backoffice.model.StatutDemande;
import com.viseo.backoffice.service.StatutDemandeService;

@Controller
@RequestMapping("/statuts-demande")
public class StatutDemandeController {

    private final StatutDemandeService statutDemandeService;

    public StatutDemandeController(StatutDemandeService statutDemandeService) {
        this.statutDemandeService = statutDemandeService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("items", statutDemandeService.findAll());
        return "statuts-demande/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("entity", new StatutDemande());
        return "statuts-demande/form";
    }

    @PostMapping
    public String create(@ModelAttribute("entity") StatutDemande entity) {
        statutDemandeService.save(entity);
        return "redirect:/statuts-demande";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        statutDemandeService.findById(id).ifPresent(entity -> model.addAttribute("entity", entity));
        return "statuts-demande/detail";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id) {
        statutDemandeService.deleteById(id);
        return "redirect:/statuts-demande";
    }
}
