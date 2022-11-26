package com.coachapp.coach_pc.service;

import com.coachapp.coach_pc.Credentials;
import com.coachapp.coach_pc.config.KeycloakConfig;
import com.coachapp.coach_pc.enums.ERole;
import com.coachapp.coach_pc.model.*;
import com.coachapp.coach_pc.repository.UserDataRepo;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;

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

    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest) {
        Keycloak keycloak = getUserToken(loginRequest.getUsername(), loginRequest.getPassword());
        AccessTokenResponse accessTokenResponse = null;

        accessTokenResponse = keycloak.tokenManager().getAccessToken();

        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setAccess_token(accessTokenResponse.getToken());
        tokenResponse.setRefresh_token(accessTokenResponse.getRefreshToken());
        tokenResponse.setToken_type(accessTokenResponse.getTokenType());
        tokenResponse.setExpires_in(accessTokenResponse.getExpiresIn());
        tokenResponse.setRefresh_expires_in(accessTokenResponse.getRefreshExpiresIn());

        Optional<UserData> userData = _userDataRepo.findByUsername(loginRequest.getUsername());

        if(userData.isEmpty()) {
            return null;
        }

        LoginResponse res = new LoginResponse(tokenResponse, userData.get());
        return new ResponseEntity<>(res, HttpStatus.OK);
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
        ResponseEntity<TokenResponse> response = getClientToken();

        CredentialRepresentation credential = Credentials.createPasswordCredentials(userDTO.getPassword());

        RealmResource instance = getInstance(response.getBody().getAccess_token(), clientId);

        UserRepresentation user = new UserRepresentation();

        user.setUsername(userDTO.getUsername());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setCredentials(Collections.singletonList(credential));
        user.setEnabled(true);
        user.setEmailVerified(true);

        UsersResource users = instance.users();

        javax.ws.rs.core.Response r = users.create(user);


        UserRepresentation savedUser = users.list()
                .stream()
                .filter(u -> u.getUsername().equals(user.getUsername()))
                .toList()
                .get(0);


        ERole role = userDTO.getRole().equals("athlete") ? ERole.athlete : ERole.coach;

        UserData userDB = new UserData();
        userDB.setCreatedAt(new Date(savedUser.getCreatedTimestamp()));
        userDB.setUpdatedAt(new Date(savedUser.getCreatedTimestamp()));
        userDB.setUsername(savedUser.getUsername().toLowerCase());
        userDB.setEmail(savedUser.getEmail().toLowerCase());
        Set<Role> newRoles = new HashSet<>();
        newRoles.add(new Role(role));
        userDB.setRoles(newRoles);

        _userDataRepo.save(userDB);

        return new ResponseEntity<>("Success", HttpStatus.SEE_OTHER);
    }

    public RealmResource getInstance(String token, String clientId){
        return KeycloakConfig.getInstance(token, clientId).realm("dev");
    }

    private ResponseEntity<TokenResponse> getClientToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);

        ResponseEntity<TokenResponse> response = restTemplate.postForEntity(
                "http://localhost:8080/realms/dev/protocol/openid-connect/token",
                httpEntity,
                TokenResponse.class
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
