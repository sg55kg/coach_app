package com.coachapp.coach_pc.service;

import com.coachapp.coach_pc.repository.UserDataRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {

    private final RestTemplate restTemplate;
    private UserDataRepo _userDataRepo;

    @Autowired
    public AuthService(RestTemplate restTemplate, UserDataRepo userDataRepo) {
        this.restTemplate = restTemplate;
        this._userDataRepo = userDataRepo;
    }


}
