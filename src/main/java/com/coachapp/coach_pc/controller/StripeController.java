package com.coachapp.coach_pc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/api/stripe")
public class StripeController {

    private Logger logger = LoggerFactory.getLogger(StripeController.class);

    @PostMapping("/onboard")
    public ResponseEntity<String> connectStripeAccount() {
        logger.info("Received request to onboard new stripe account");
        return null;
    }
}
