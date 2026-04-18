package com.viseo.backoffice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.viseo.backoffice.model.Demandeur;
import com.viseo.backoffice.service.DemandeurService;

@Controller
@RequestMapping("/demandeurs")
public class DemandeurController {

    private final DemandeurService demandeurService;

    public DemandeurController(DemandeurService demandeurService) {
        this.demandeurService = demandeurService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("items", demandeurService.findAll());
        return "demandeurs/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("entity", new Demandeur());
        return "demandeurs/form";
    }

    @PostMapping
    public String create(@ModelAttribute("entity") Demandeur entity) {
        demandeurService.save(entity);
        return "redirect:/demandeurs";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        demandeurService.findById(id).ifPresent(entity -> model.addAttribute("entity", entity));
        return "demandeurs/detail";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id) {
        demandeurService.deleteById(id);
        return "redirect:/demandeurs";
    }
}
