package com.coachapp.coach_pc.controller;

import com.coachapp.coach_pc.model.*;
import com.coachapp.coach_pc.request.LoginRequest;
import com.coachapp.coach_pc.request.TokenRequest;
import com.coachapp.coach_pc.service.AuthService;
import com.coachapp.coach_pc.view.LoginResponse;
import com.coachapp.coach_pc.view.Response;
import com.coachapp.coach_pc.view.TokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {

    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        ResponseEntity<LoginResponse> res =  authService.login(loginRequest);
        if(res == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            return res;
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Response> logout(@RequestBody TokenRequest tokenRequest) {
        return authService.logout(tokenRequest);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register( @RequestBody UserDTO userDTO) {
        if (!userDTO.getRole().equals("athlete") && !userDTO.getRole().equals("coach")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return authService.register(userDTO);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestBody TokenRequest tokenRequest) {
        return authService.refresh(tokenRequest);
    }
}
