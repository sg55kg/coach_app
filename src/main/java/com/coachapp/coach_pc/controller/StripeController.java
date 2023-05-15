package com.coachapp.coach_pc.controller;

import com.coachapp.coach_pc.request.payment.NewStripeAccountRequest;
import com.coachapp.coach_pc.service.payment.StripeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/stripe")
public class StripeController {

    private Logger logger = LoggerFactory.getLogger(StripeController.class);
    private StripeService service;

    public StripeController(StripeService service) {
        this.service = service;
    }

    @PostMapping("/onboard")
    public ResponseEntity<String> connectStripeAccount(@RequestBody NewStripeAccountRequest request) {
        logger.info("Received request to onboard new stripe account");
        return this.service.connectNewStripAccount(request);
    }

    @GetMapping("{stripeAccountId}/onboard")
    public ResponseEntity<String> initiateOnboarding(@PathVariable String stripeAccountId) {
        return this.service.createAccountLink(stripeAccountId);
    }
}
