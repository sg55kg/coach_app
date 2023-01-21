package com.coachapp.coach_pc.controller;

import com.coachapp.coach_pc.model.UserData;
import com.coachapp.coach_pc.request.NewAthleteRequest;
import com.coachapp.coach_pc.request.NewCoachRequest;
import com.coachapp.coach_pc.request.NewUserRequest;
import com.coachapp.coach_pc.request.UpdateUserRequest;
import com.coachapp.coach_pc.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/api/users")
public class UsersController {

    private UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserData> getUserData(@PathVariable String email) {
        return userService.getUserData(email);
    }

    @PostMapping
    public ResponseEntity<UserData> addUser(@RequestBody NewUserRequest userRequest) {
        return userService.addUser(userRequest);
    }

    @PutMapping("/coach")
    public ResponseEntity<UserData> addCoachData(@RequestBody NewCoachRequest coachRequest) {
        return userService.addCoachData(coachRequest);
    }

    @PutMapping("/athlete")
    public ResponseEntity<UserData> addAthleteData(@RequestBody NewAthleteRequest athleteRequest) {
        return userService.addAthleteData(athleteRequest);
    }

    @PutMapping("{id}")
    public ResponseEntity<UserData> updateUserData(@PathVariable UUID id, @RequestBody UpdateUserRequest userRequest) {
        return userService.updateUserData(id, userRequest);
    }
}
