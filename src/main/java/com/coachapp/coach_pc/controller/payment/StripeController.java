package com.coachapp.coach_pc.controller.payment;

import com.coachapp.coach_pc.request.payment.AthletePaymentRequest;
import com.coachapp.coach_pc.request.payment.NewStripeAccountRequest;
import com.coachapp.coach_pc.service.payment.StripeService;
import com.coachapp.coach_pc.view.payment.TeamFinanceWithIds;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/stripe")
public class StripeController {

    private final Logger logger = LoggerFactory.getLogger(StripeController.class);
    private final StripeService service;
    @Value("${stripe-signed-secret}")
    private String STRIPE_SIGNED_SECRET;

    public StripeController(StripeService service) {
        this.service = service;
    }

    @PostMapping("/connect")
    public ResponseEntity<TeamFinanceWithIds> connectStripeAccount(@RequestBody NewStripeAccountRequest request) {
        logger.info("Received request to create a new stripe account");
        return this.service.connectNewStripeAccount(request);
    }

    @GetMapping("/{stripeConnectId}")
    public ResponseEntity<String> getStripeAccount(@PathVariable String stripeConnectId) {
        logger.info("Received request to retrieve Stripe account");
        return this.service.getStripeAccount(stripeConnectId);
    }

    @GetMapping("/{stripeConnectId}/onboard")
    public ResponseEntity<String> initiateOnboarding(@PathVariable String stripeConnectId,
                                                     @RequestParam String returnUrl) {
        logger.info("Received request to initiate onboarding for connected Stripe account");
        return this.service.createAccountLink(stripeConnectId, returnUrl);
    }

    @PostMapping("/{stripeConnectId}/payment")
    public ResponseEntity<String> createAthleteTeamSubscription(@PathVariable String stripeConnectId,
                                                                @RequestBody AthletePaymentRequest request) {
        logger.info("Received request for athlete " + request.getAthleteEmail() + " on team " + request.getTeamId());
        return this.service.createStripeCheckoutSession(stripeConnectId, request);
    }

    @PostMapping("/webhook")
    public ResponseEntity<?> handleStripeWebhook(@RequestBody String payload,
                                                 @RequestHeader("Stripe-Signature") String sigHeader) {
        try {
            Event event = Webhook.constructEvent(payload, sigHeader, STRIPE_SIGNED_SECRET);
            return service.handleStripeWebhook(event);
        } catch (SignatureVerificationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("{stripeConnectId}")
    public ResponseEntity<?> updateStripeAccount(@PathVariable String stripeConnectId) {
        return null; // TODO: Request model, service method, response, frontend model(?)
    }
}
