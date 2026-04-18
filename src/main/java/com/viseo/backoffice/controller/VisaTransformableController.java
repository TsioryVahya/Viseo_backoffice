package com.viseo.backoffice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.viseo.backoffice.model.VisaTransformable;
import com.viseo.backoffice.service.VisaTransformableService;

@Controller
@RequestMapping("/visas-transformables")
public class VisaTransformableController {

    private final VisaTransformableService visaTransformableService;

    public VisaTransformableController(VisaTransformableService visaTransformableService) {
        this.visaTransformableService = visaTransformableService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("items", visaTransformableService.findAll());
        return "visas-transformables/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("entity", new VisaTransformable());
        return "visas-transformables/form";
    }

    @PostMapping
    public String create(@ModelAttribute("entity") VisaTransformable entity) {
        visaTransformableService.save(entity);
        return "redirect:/visas-transformables";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        visaTransformableService.findById(id).ifPresent(entity -> model.addAttribute("entity", entity));
        return "visas-transformables/detail";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id) {
        visaTransformableService.deleteById(id);
        return "redirect:/visas-transformables";
    }
}
