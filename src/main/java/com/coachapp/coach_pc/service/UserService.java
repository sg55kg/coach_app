package com.coachapp.coach_pc.service;

import com.coachapp.coach_pc.model.UserData;
import com.coachapp.coach_pc.repository.UserDataRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private UserDataRepo userDataRepo;

    @Autowired
    public UserService(UserDataRepo userDataRepo) {
        this.userDataRepo = userDataRepo;
    }

    public ResponseEntity<UserData> getUserData(String email) {
        Optional<UserData> optional = userDataRepo.findByEmail(email);

        if(optional.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(optional.get(), HttpStatus.OK);
        }
    }

    public ResponseEntity<UserData> addUser(String email, String name) {
        UserData userData = new UserData();
        userData.setEmail(email);
        userData.setUsername(name);

        UserData dbUser = userDataRepo.save(userData);
        return new ResponseEntity<>(dbUser, HttpStatus.CREATED);
    }
}
