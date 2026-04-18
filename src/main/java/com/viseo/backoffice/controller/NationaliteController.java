package com.viseo.backoffice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.viseo.backoffice.model.Nationalite;
import com.viseo.backoffice.service.NationaliteService;

@Controller
@RequestMapping("/nationalites")
public class NationaliteController {

    private final NationaliteService nationaliteService;

    public NationaliteController(NationaliteService nationaliteService) {
        this.nationaliteService = nationaliteService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("items", nationaliteService.findAll());
        return "nationalites/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("entity", new Nationalite());
        return "nationalites/form";
    }

    @PostMapping
    public String create(@ModelAttribute("entity") Nationalite entity) {
        nationaliteService.save(entity);
        return "redirect:/nationalites";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        nationaliteService.findById(id).ifPresent(entity -> model.addAttribute("entity", entity));
        return "nationalites/detail";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id) {
        nationaliteService.deleteById(id);
        return "redirect:/nationalites";
    }
}
