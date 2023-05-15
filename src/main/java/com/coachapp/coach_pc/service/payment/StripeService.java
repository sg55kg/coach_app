package com.coachapp.coach_pc.service.payment;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.param.AccountCreateParams;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

    private String STRIPE_API_KEY = "sk_test_51MTzH1JNTyetRpYc10q46o3FyoW8EPAIBeGBOlafvSj8wd14G7lJPq6Lu131EvUs3PzVY3r3i7QDJOyQ5nADS3Ov00pDn36ALL";

    public ResponseEntity<String> connectNewStripAccount() {
        Stripe.apiKey = STRIPE_API_KEY;

        AccountCreateParams params =
                AccountCreateParams.builder().setType(AccountCreateParams.Type.STANDARD).build();

        try {
            Account account = Account.create(params);
        } catch (StripeException e) {
            return null;
        }
    }
}
