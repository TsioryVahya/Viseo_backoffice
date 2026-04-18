package com.viseo.backoffice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.viseo.backoffice.model.Visa;
import com.viseo.backoffice.service.VisaService;

@Controller
@RequestMapping("/visas")
public class VisaController {

    private final VisaService visaService;

    public VisaController(VisaService visaService) {
        this.visaService = visaService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("items", visaService.findAll());
        return "visas/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("entity", new Visa());
        return "visas/form";
    }

    @PostMapping
    public String create(@ModelAttribute("entity") Visa entity) {
        visaService.save(entity);
        return "redirect:/visas";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        visaService.findById(id).ifPresent(entity -> model.addAttribute("entity", entity));
        return "visas/detail";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id) {
        visaService.deleteById(id);
        return "redirect:/visas";
    }
}
