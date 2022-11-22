package com.coachapp.coach_pc.service;

import com.coachapp.coach_pc.Credentials;
import com.coachapp.coach_pc.config.KeycloakConfig;
import com.coachapp.coach_pc.enums.UserType;
import com.coachapp.coach_pc.model.*;
import com.coachapp.coach_pc.repository.UserDataRepo;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final RestTemplate restTemplate;
    @Value("${spring.security.oauth2.client.provider.keycloak.issuer-uri}")
    private String issueUrl;

    @Value("${spring.security.oauth2.client.registration.oauth2-client-credentials.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.oauth2-client-credentials.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.oauth2-client-credentials.authorization-grant-type}")
    private String grantType;
    private String keycloakUserInfoUrl = "http://localhost:8080/realms/dev/protocol/openid-connect/userinfo";
    private UserDataRepo _userDataRepo;

    @Autowired
    public AuthService(RestTemplate restTemplate, UserDataRepo userDataRepo) {
        this.restTemplate = restTemplate;
        this._userDataRepo = userDataRepo;
    }

    public ResponseEntity<AccessTokenResponse> login(LoginRequest loginRequest) {
        Keycloak keycloak = getUserToken(loginRequest.getUsername(), loginRequest.getPassword());
        AccessTokenResponse accessTokenResponse = null;

        accessTokenResponse = keycloak.tokenManager().getAccessToken();

        return new ResponseEntity<>(accessTokenResponse, HttpStatus.OK);

        //http://localhost:8080/realms/dev/protocol/openid-connect/userinfo

//        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
//        map.add("client_id", clientId);
//        map.add("client_secret", clientSecret);
//        map.add("grant_type", "client_credentials");
//
//        HttpHeaders h1 = new HttpHeaders();
//        h1.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, h1);
//
//        ResponseEntity<LoginResponse> response = restTemplate.postForEntity(
//                "http://localhost:8080/realms/dev/protocol/openid-connect/token",
//                httpEntity,
//                LoginResponse.class
//        );

//        h.setBearerAuth(response.getBody().getAccess_token());
//        h.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//    //    headers.add("Authorization", response.getBody().getAccess_token());
//        body.add("client_id", clientId);
//        body.add("client_secret", clientSecret);
//
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, h);
//        ResponseEntity<String> s = restTemplate.getForEntity(keycloakUserInfoUrl, String.class, request);
//       // String s = restTemplate.getForObject(keycloakUserInfoUrl, String.class, headers);
//        System.out.println(s.getBody());
//        keycloak.realm("dev").users().get().
    }

    public ResponseEntity<Response> logout(TokenRequest tokenRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("refresh_token", tokenRequest.getToken());

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);

        ResponseEntity<Response> response = restTemplate.postForEntity(
                "http://localhost:8080/realms/dev/protocol/openid-connect/logout",
                httpEntity,
                Response.class
        );

        Response res = new Response();

        if(response.getStatusCode().is2xxSuccessful()) {
            res.setMessage("Logged out");
        } else {
            res.setMessage("Error");
        }

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    public ResponseEntity<String> register(UserDTO userDTO) {
        ResponseEntity<LoginResponse> response = getClientToken();

            CredentialRepresentation credential = Credentials.createPasswordCredentials(userDTO.getPassword());

            RealmResource instance = getInstance(response.getBody().getAccess_token(), clientId);

            UserRepresentation user = new UserRepresentation();

            user.setUsername(userDTO.getUsername());
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setEmail(userDTO.getEmail());
            user.setCredentials(Collections.singletonList(credential));
            user.setEnabled(true);

            UsersResource users = instance.users();

            javax.ws.rs.core.Response r = users.create(user);


            UserRepresentation savedUser = users.list().stream().filter(u -> u.getUsername().equals(user.getUsername())).collect(Collectors.toList()).get(0);
            String userId = savedUser.getId();
            UserType type = userDTO.getRole().equals("athlete") ? UserType.ATHLETE : UserType.COACH;
            savedUser.setRealmRoles(Collections.singletonList(userDTO.getRole()));
            users.get(userId).update(savedUser);

            UserData userDB = new UserData(userId,
                    new Date(savedUser.getCreatedTimestamp()),
                    new Date(savedUser.getCreatedTimestamp()),
                    type,
                    savedUser.getEmail()
            );
            _userDataRepo.save(userDB);

            return new ResponseEntity<>("Success", HttpStatus.SEE_OTHER);
    }

    public RealmResource getInstance(String token, String clientId){
        return KeycloakConfig.getInstance(token, clientId).realm("dev");
    }

    private ResponseEntity<LoginResponse> getClientToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);

        ResponseEntity<LoginResponse> response = restTemplate.postForEntity(
                "http://localhost:8080/realms/dev/protocol/openid-connect/token",
                httpEntity,
                LoginResponse.class
        );

        return response;
    }

    private Keycloak getUserToken(String username, String password) {
        return KeycloakConfig.getUsernameAndPasswordInstance(
                clientId,
                clientSecret,
                username,
                password
        );
    }
}
