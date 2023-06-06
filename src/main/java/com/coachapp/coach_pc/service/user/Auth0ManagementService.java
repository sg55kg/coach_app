package com.coachapp.coach_pc.service.user;

import com.coachapp.coach_pc.model.team.Team;
import com.coachapp.coach_pc.model.user.AthleteData;
import com.coachapp.coach_pc.model.user.CoachData;
import com.coachapp.coach_pc.model.user.UserData;
import com.coachapp.coach_pc.repository.user.UserRepository;
import com.coachapp.coach_pc.request.user.InviteUserRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class Auth0ManagementService {

    @Value("${auth0.domain}")
    private String auth0Domain;

    @Value("${auth0.clientId}")
    private String auth0ClientId;

    @Value("${auth0.clientSecret}")
    private String auth0ClientSecret;

    @Value("${auth0.m2m.clientSecret}")
    private String auth0M2MClientSecret;

    @Value("${auth0.m2m.clientId}")
    private String auth0M2MClientId;

    private final UserRepository repository;

    private final Logger logger = LoggerFactory.getLogger(Auth0ManagementService.class);

    public Auth0ManagementService(UserRepository repository) {
        this.repository = repository;
    }

    public ResponseEntity<String> createUserAccount(InviteUserRequest request) {
        // TODO: first check if the user has an existing account with us, and skip auth0 registration if necessary
        try {
            String token = getAuth0AccessToken();
            Map<String, Object> createdUser = createAuth0Account(token, request);

            saveUserData(request);
            String auth0UserId = (String)createdUser.get("user_id");
            sendInvitationEmail(auth0UserId, token);

            return new ResponseEntity<>("Successfully invited user: " + request.getAthleteName(), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    private void saveUserData(InviteUserRequest request) {
        UserData userToSave = new UserData();
        CoachData coach = new CoachData();
        coach.setId(request.getCoachId());
        Team team = new Team();
        team.setId(request.getTeamId());
        AthleteData athleteData = new AthleteData();
        athleteData.setCoach(coach);
        athleteData.setTeam(team);
        athleteData.setName(request.getAthleteName());

        userToSave.setEmail(request.getAthleteEmail());
        userToSave.setUsername(request.getAthleteName());
        userToSave.setAthleteData(athleteData);
        athleteData.setUser(userToSave);
        repository.addUser(userToSave);
    }

    private void sendInvitationEmail(String userId, String token) {
        // TODO: investigate if query params are needed
        Integer FIVE_DAYS = 432000;
        String res = WebClient.create(auth0Domain)
                .post()
                .uri("api/v2/tickets/password-change")
                .bodyValue("{ \"user_id\": \"" + userId + "\", " +
                        "\"ttl_sec\": \"" + FIVE_DAYS + "\", " +
                        "\"client_id\": " + auth0ClientId +
                        "\"result_url\": \"https://localhost:5173/home\" }")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        System.out.println(res);
    }

    private Map<String, Object> createAuth0Account(String token, InviteUserRequest request) throws JsonProcessingException {
        String password = getSaltString();
        String username = request.getAthleteName();
        String athleteEmail = request.getAthleteEmail();

        try {
            String createdUser = WebClient.create(auth0Domain)
                    .post()
                    .uri("api/v2/users")
                    .bodyValue("{\"email\": \"" + athleteEmail + "\"," +
                            "\"user_metadata\": {}," +
                            "\"blocked\": false," +
                            "\"email_verified\": false," +
                            "\"app_metadata\": {}," +
                            "\"name\": \"" + username + "\"," +
                            "\"connection\": \"Username-Password-Authentication\"," +
                            "\"password\": \"" + password + "\"," +
                            "\"verify_email\": false," +
                            "\"username\": \"" + athleteEmail + "\"}")
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            TypeReference<Map<String, Object>> typeRef = new TypeReference<>() {};
            return new ObjectMapper().readValue(createdUser, typeRef);
        } catch (JsonProcessingException e) {
            logger.error("Error parsing JSON response during new user creation");
            throw e;
        } catch (Exception e) {
            logger.error("Error creating new user: " + e.getMessage());
            throw e;
        }
    }

    private String getAuth0AccessToken() {
        String body = "grant_type=client_credentials&client_id=" + auth0M2MClientId +
                "&client_secret=" + auth0M2MClientSecret + "&audience=" + auth0Domain + "api/v2/";
        try {
            Map request = WebClient.create(auth0Domain)
                    .post()
                    .uri("oauth/token")
                    .bodyValue(body)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .retrieve().bodyToMono(Map.class).block();

            return (String)request.get("access_token");
        } catch (Exception e) {
            logger.error("Error retrieving m2m access token: " + e.getMessage());
            throw e;
        }
    }

    private String getSaltString() {
        String SALT_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALT_CHARS.length());
            salt.append(SALT_CHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }
}
