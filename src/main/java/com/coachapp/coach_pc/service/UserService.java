package com.coachapp.coach_pc.service;

import com.coachapp.coach_pc.model.CoachData;
import com.coachapp.coach_pc.model.UserData;
import com.coachapp.coach_pc.repository.UserDataRepo;
import com.coachapp.coach_pc.request.NewCoachRequest;
import com.coachapp.coach_pc.request.NewUserRequest;
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

    public ResponseEntity<UserData> addUser(NewUserRequest userRequest) {
        UserData userData = new UserData();
        userData.setEmail(userRequest.getEmail());
        userData.setUsername(userRequest.getName());

        UserData dbUser = userDataRepo.save(userData);
        return new ResponseEntity<>(dbUser, HttpStatus.CREATED);
    }


    public ResponseEntity<UserData> addCoachData(NewCoachRequest coachRequest) {
        boolean exists = userDataRepo.existsById(coachRequest.getUserId());
        if (!exists) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        CoachData coach = new CoachData();
        UserData user = new UserData();

        user.setEmail(coachRequest.getEmail());
        user.setId(coachRequest.getUserId());
        user.setUsername(coachRequest.getUsername());
        coach.setUser(user);
        user.setCoachData(coach);

        user = userDataRepo.save(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
