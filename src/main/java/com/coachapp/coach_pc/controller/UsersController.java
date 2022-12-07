package com.coachapp.coach_pc.controller;

import com.coachapp.coach_pc.model.UserData;
import com.coachapp.coach_pc.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<UserData> addUser(@RequestBody String email, @RequestBody String name) {
        return userService.addUser(email, name);
    }
}
