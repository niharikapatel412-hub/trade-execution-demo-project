package com.example.order.execution.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UiController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("pageTitle", "FX Order Execution");
        return "orders"; // This maps to src/main/resources/templates/index.html
    }
}
