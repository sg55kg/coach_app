package com.coachapp.coach_pc.service.payment;

import com.coachapp.coach_pc.repository.payment.PaymentRepository;
import com.coachapp.coach_pc.request.payment.NewStripeAccountRequest;
import com.coachapp.coach_pc.view.payment.TeamFinanceWithIds;
import com.coachapp.coach_pc.view.payment.TeamFinanceWithIdsImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StripeServiceTest {

    @InjectMocks
    private StripeService service;
    @Mock
    private PaymentRepository repository;

    @Test
    void connectNewStripAccount() {
        TeamFinanceWithIds finance = new TeamFinanceWithIdsImpl(UUID.randomUUID());
        when(repository.createTeamFinance(any(NewStripeAccountRequest.class), any(String.class))).thenReturn(finance);

        NewStripeAccountRequest request = new NewStripeAccountRequest();
        UUID teamId = UUID.randomUUID();
        UUID coachId = UUID.randomUUID();

        request.setCoachId(coachId);
        request.setEmail("test@test.com");
        request.setCurrency("USD");
        request.setCountryCode("US");
        request.setUsername("test user");
        request.setTeamId(teamId);
        ResponseEntity<TeamFinanceWithIds> res = service.connectNewStripeAccount(request);
        Assertions.assertNotNull(res);
        assertEquals(HttpStatus.OK, res.getStatusCode());
    }

    @Test
    void createAccountLink() {
    }

    @Test
    void getStripeAccount() {
    }

    @Test
    void createStripeCheckoutSession() {
    }

    @Test
    void handleStripeWebhook() {
    }
}