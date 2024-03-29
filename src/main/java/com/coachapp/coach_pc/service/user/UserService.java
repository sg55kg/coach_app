package com.coachapp.coach_pc.service.user;

import com.coachapp.coach_pc.model.user.AthleteData;
import com.coachapp.coach_pc.model.user.CoachData;
import com.coachapp.coach_pc.model.team.Team;
import com.coachapp.coach_pc.model.user.UserData;
import com.coachapp.coach_pc.repository.user.UserRepository;
import com.coachapp.coach_pc.request.team.NewTeamRequest;
import com.coachapp.coach_pc.request.user.NewAthleteRequest;
import com.coachapp.coach_pc.request.user.NewCoachRequest;
import com.coachapp.coach_pc.request.user.NewUserRequest;
import com.coachapp.coach_pc.request.user.UpdateUserRequest;
import com.coachapp.coach_pc.view.user.UserWithMappings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private UserRepository userRepo;
    private Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public ResponseEntity<UserWithMappings> getUserData(String email) {
        Optional<UserWithMappings> optional = userRepo.findUserByEmail(email);

        if(optional.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            UserWithMappings user = optional.get();
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
    }

    public ResponseEntity<UserWithMappings> addUser(NewUserRequest userRequest) {
        UserData userData = new UserData();
        userData.setEmail(userRequest.getEmail());
        userData.setUsername(userRequest.getName());
        userData.setPhotoUrl(userRequest.getPhotoUrl());
        // Connect both sides of the bi-directional relationship
        userData.setPreferences(userRequest.getPreferences());
        userData.getPreferences().setUser(userData);

        UserWithMappings dbUser = userRepo.addUser(userData);
        return new ResponseEntity<>(dbUser, HttpStatus.CREATED);
    }


    public ResponseEntity<UserWithMappings> addCoachData(NewCoachRequest coachRequest) {
        Optional<UserData> optional = userRepo.findUserById(coachRequest.getUserId());
        if (optional.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        CoachData coach = new CoachData();
        UserData user = optional.get();
        Team team = NewTeamRequest.convertRequest(coachRequest.getTeam(), coach);
        coach.setUser(user);
        user.setCoachData(coach);

        UserWithMappings updated = userRepo.updateUser(user);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    public ResponseEntity<UserWithMappings> addAthleteData(NewAthleteRequest athleteRequest) {
        Optional<UserData> o = userRepo.findUserById(athleteRequest.getUserId());
        if(o.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        AthleteData athlete = new AthleteData();
        UserData user = o.get();
        athlete.setUser(user);
        athlete.setName(user.getUsername());
        user.setAthleteData(athlete);

        UserWithMappings updated = userRepo.updateUser(user);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    public ResponseEntity<UserWithMappings> updateUserData(UUID id, UpdateUserRequest userRequest) {
        Optional<UserData> optional = userRepo.findUserById(id);
        if (optional.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        UserData user = optional.get();
        logger.info("User retrieved");
        user.setUsername(userRequest.getUsername());
        if (userRequest.getPhotoUrl() != null) {
            user.setPhotoUrl(userRequest.getPhotoUrl());
        }
        if (userRequest.getAthleteName() != null) {
            user.getAthleteData().setName(userRequest.getAthleteName());
        }

        userRequest.getPreferences().setUser(user);
        user.setPreferences(userRequest.getPreferences());

        UserWithMappings updated = userRepo.updateUser(user);
        logger.info("User updated");
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }
}
