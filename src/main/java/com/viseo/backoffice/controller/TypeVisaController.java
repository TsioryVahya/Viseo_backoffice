package com.viseo.backoffice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.viseo.backoffice.model.TypeVisa;
import com.viseo.backoffice.service.TypeVisaService;

@Controller
@RequestMapping("/types-visa")
public class TypeVisaController {

    private final TypeVisaService typeVisaService;

    public TypeVisaController(TypeVisaService typeVisaService) {
        this.typeVisaService = typeVisaService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("items", typeVisaService.findAll());
        return "types-visa/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("entity", new TypeVisa());
        return "types-visa/form";
    }

    @PostMapping
    public String create(@ModelAttribute("entity") TypeVisa entity) {
        typeVisaService.save(entity);
        return "redirect:/types-visa";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        typeVisaService.findById(id).ifPresent(entity -> model.addAttribute("entity", entity));
        return "types-visa/detail";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id) {
        typeVisaService.deleteById(id);
        return "redirect:/types-visa";
    }
}
