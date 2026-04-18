package com.viseo.backoffice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.viseo.backoffice.model.SituationFamiliale;
import com.viseo.backoffice.service.SituationFamilialeService;

@Controller
@RequestMapping("/situations-familiales")
public class SituationFamilialeController {

    private final SituationFamilialeService situationFamilialeService;

    public SituationFamilialeController(SituationFamilialeService situationFamilialeService) {
        this.situationFamilialeService = situationFamilialeService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("items", situationFamilialeService.findAll());
        return "situations-familiales/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("entity", new SituationFamiliale());
        return "situations-familiales/form";
    }

    @PostMapping
    public String create(@ModelAttribute("entity") SituationFamiliale entity) {
        situationFamilialeService.save(entity);
        return "redirect:/situations-familiales";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        situationFamilialeService.findById(id).ifPresent(entity -> model.addAttribute("entity", entity));
        return "situations-familiales/detail";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id) {
        situationFamilialeService.deleteById(id);
        return "redirect:/situations-familiales";
    }
}
