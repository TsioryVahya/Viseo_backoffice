package com.viseo.backoffice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.viseo.backoffice.model.Passeport;
import com.viseo.backoffice.service.PasseportService;

@Controller
@RequestMapping("/passeports")
public class PasseportController {

    private final PasseportService passeportService;

    public PasseportController(PasseportService passeportService) {
        this.passeportService = passeportService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("items", passeportService.findAll());
        return "passeports/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("entity", new Passeport());
        return "passeports/form";
    }

    @PostMapping
    public String create(@ModelAttribute("entity") Passeport entity) {
        passeportService.save(entity);
        return "redirect:/passeports";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        passeportService.findById(id).ifPresent(entity -> model.addAttribute("entity", entity));
        return "passeports/detail";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id) {
        passeportService.deleteById(id);
        return "redirect:/passeports";
    }
}
