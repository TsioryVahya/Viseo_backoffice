package com.viseo.backoffice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.viseo.backoffice.model.StatutDemandeType;
import com.viseo.backoffice.service.StatutDemandeTypeService;

@Controller
@RequestMapping("/statuts-demande-type")
public class StatutDemandeTypeController {

    private final StatutDemandeTypeService statutDemandeTypeService;

    public StatutDemandeTypeController(StatutDemandeTypeService statutDemandeTypeService) {
        this.statutDemandeTypeService = statutDemandeTypeService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("items", statutDemandeTypeService.findAll());
        return "statuts-demande-type/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("entity", new StatutDemandeType());
        return "statuts-demande-type/form";
    }

    @PostMapping
    public String create(@ModelAttribute("entity") StatutDemandeType entity) {
        statutDemandeTypeService.save(entity);
        return "redirect:/statuts-demande-type";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        statutDemandeTypeService.findById(id).ifPresent(entity -> model.addAttribute("entity", entity));
        return "statuts-demande-type/detail";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id) {
        statutDemandeTypeService.deleteById(id);
        return "redirect:/statuts-demande-type";
    }
}
