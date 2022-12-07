package com.coachapp.coach_pc.controller;

import com.coachapp.coach_pc.model.Day;
import com.coachapp.coach_pc.service.DayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/days")
@CrossOrigin
public class DayController {

    private DayService _dayService;

    @Autowired
    public DayController(DayService dayService) {
        this._dayService = dayService;
    }

    @GetMapping("/{id}")
    public Day getDay(@PathVariable UUID id) {
        return this._dayService.getDay(id);
    }

    @PutMapping("/{id}")
    public Day updateDay(@PathVariable Day day) {
        return this._dayService.updateDay(day);
    }
}
