package com.coachapp.coach_pc.controller;

import com.coachapp.coach_pc.model.*;
import com.coachapp.coach_pc.request.TokenRequest;
import com.coachapp.coach_pc.service.AuthService;
import com.coachapp.coach_pc.view.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }


}
