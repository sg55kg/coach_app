package com.coachapp.coach_pc.controller.user;

import com.coachapp.coach_pc.request.user.InviteUserRequest;
import com.coachapp.coach_pc.service.user.Auth0ManagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/auth")
public class AuthController {

    private final Auth0ManagementService service;

    public AuthController(Auth0ManagementService service) {
        this.service = service;
    }

    @PostMapping("/invite")
    public ResponseEntity<String> inviteAthlete(@RequestBody InviteUserRequest request) {
        return service.createUserAccount(request);
    }
}
