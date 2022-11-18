package com.coachapp.coach_pc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/")
@CrossOrigin("*")
public class PageController {

    @GetMapping
    public String index() {
        return "index";
    }

    @GetMapping("{id}")
    public String program(@PathVariable UUID id) {
        return "program";
    }
}

// test
