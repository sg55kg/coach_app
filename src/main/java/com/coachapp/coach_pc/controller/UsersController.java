package com.coachapp.coach_pc.controller;

import com.coachapp.coach_pc.model.UserData;
import com.coachapp.coach_pc.request.NewAthleteRequest;
import com.coachapp.coach_pc.request.NewCoachRequest;
import com.coachapp.coach_pc.request.NewUserRequest;
import com.coachapp.coach_pc.request.UpdateUserRequest;
import com.coachapp.coach_pc.service.UserService;
import com.coachapp.coach_pc.view.user.UserViewModel;
import com.coachapp.coach_pc.view.user.UserWithMappings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private Logger logger = LoggerFactory.getLogger(UsersController.class);

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserWithMappings> getUserData(@PathVariable String email) {
        logger.info("Received request to fetch user data for user: " + email);
        return userService.getUserData(email);
    }

    @PostMapping
    public ResponseEntity<UserWithMappings> addUser(@RequestBody NewUserRequest userRequest) {
        logger.info("Received request to create a new user with email: " + userRequest.getEmail());
        return userService.addUser(userRequest);
    }

    @PutMapping("/coach")
    public ResponseEntity<UserWithMappings> addCoachData(@RequestBody NewCoachRequest coachRequest) {
        logger.info("Received request to create coach data for user: " + coachRequest.getUserId());
        return userService.addCoachData(coachRequest);
    }

    @PutMapping("/athlete")
    public ResponseEntity<UserWithMappings> addAthleteData(@RequestBody NewAthleteRequest athleteRequest) {
        logger.info("Received request to create athlete data for user: " + athleteRequest.getUserId());
        return userService.addAthleteData(athleteRequest);
    }

    @PutMapping("{id}")
    public ResponseEntity<UserWithMappings> updateUserData(@PathVariable UUID id, @RequestBody UpdateUserRequest userRequest) {
        return userService.updateUserData(id, userRequest);
    }
}
