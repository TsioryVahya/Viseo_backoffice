package com.viseo.backoffice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.viseo.backoffice.model.Demande;
import com.viseo.backoffice.service.DemandeService;

@Controller
@RequestMapping("/demandes")
public class DemandeController {

    private final DemandeService demandeService;

    public DemandeController(DemandeService demandeService) {
        this.demandeService = demandeService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("items", demandeService.findAll());
        return "demandes/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("entity", new Demande());
        return "demandes/form";
    }

    @PostMapping
    public String create(@ModelAttribute("entity") Demande entity) {
        demandeService.save(entity);
        return "redirect:/demandes";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        model.addAttribute("entity", demandeService.findById(id));
        return "demandes/detail";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id) {
        demandeService.deleteById(id);
        return "redirect:/demandes";
    }
}
