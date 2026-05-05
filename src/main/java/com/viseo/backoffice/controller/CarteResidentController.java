package com.viseo.backoffice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.viseo.backoffice.model.CarteResident;
import com.viseo.backoffice.service.CarteResidentService;

@Controller
@RequestMapping("/cartes-resident")
public class CarteResidentController {

    private final CarteResidentService carteResidentService;

    public CarteResidentController(CarteResidentService carteResidentService) {
        this.carteResidentService = carteResidentService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("items", carteResidentService.findAll());
        return "cartes-resident/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("entity", new CarteResident());
        return "cartes-resident/form";
    }

    @PostMapping
    public String create(@ModelAttribute("entity") CarteResident entity) {
        carteResidentService.save(entity);
        return "redirect:/cartes-resident";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        model.addAttribute("entity", carteResidentService.findById(id));
        return "cartes-resident/detail";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id) {
        carteResidentService.deleteById(id);
        return "redirect:/cartes-resident";
    }
}
