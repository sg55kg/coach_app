package com.coachapp.coach_pc.controller.user;

import com.coachapp.coach_pc.request.user.InviteUserRequest;
import com.coachapp.coach_pc.service.email.SESService;
import com.coachapp.coach_pc.service.user.Auth0ManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


// Recommended to make arguments final - you want to avoid accidental modification of inputs
// e.g getChatRoom(final @PathVariable UUID id)
@Controller
@RequestMapping("/api/auth")
public class AuthController {

    private final Auth0ManagementService authService;
    private final SESService emailService;

    public AuthController(Auth0ManagementService authService, SESService emailService) {
        this.authService = authService;
        this.emailService = emailService;
    }

    @PostMapping("/invite")
    public ResponseEntity<String> inviteAthlete(@RequestBody InviteUserRequest request) {
        String inviteUrl = authService.inviteUser(request);

        if (inviteUrl == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        if (inviteUrl.startsWith("User")) {
            return new ResponseEntity<>("User " + request.getAthleteName() + " already exists", HttpStatus.OK);
        }
        // TODO: add team_member table to join athlete <-> team, keeps status like INVITED, CURRENT, BLOCKED, UNPAID
        // TODO: Come up with migration to add team_member rows w/out messing up current athletes in teams
        emailService.sendUserInviteEmail(inviteUrl, request);
        return new ResponseEntity<>("Successfully invited " + request.getAthleteName() + "!", HttpStatus.CREATED);
    }
}
