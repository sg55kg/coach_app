package com.coachapp.coach_pc.service.payment;

import com.coachapp.coach_pc.repository.PaymentRepository;
import com.coachapp.coach_pc.request.payment.NewStripeAccountRequest;
import com.coachapp.coach_pc.view.payment.TeamFinanceViewModel;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.AccountLink;
import com.stripe.param.AccountCreateParams;
import com.stripe.param.AccountLinkCreateParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

    @Value("${stripe-api-key}")
    private String STRIPE_API_KEY;
    @Value("${client-url}")
    private String CLIENT_URL;
    private final Logger logger = LoggerFactory.getLogger(StripeService.class);
    private final PaymentRepository repository;

    public StripeService(PaymentRepository repository) {
        this.repository = repository;
    }

    public ResponseEntity<String> connectNewStripAccount(NewStripeAccountRequest request) {
        Stripe.apiKey = STRIPE_API_KEY;

        AccountCreateParams params =
                AccountCreateParams.builder()
                        .setType(AccountCreateParams.Type.STANDARD)
                        .setEmail(request.getEmail())
                        .setCountry(request.getCountryCode())
                        .setDefaultCurrency(request.getCurrency())
                        .build();

        try {
            Account account = Account.create(params);
            String stripeAccountId = account.getId();

            TeamFinanceViewModel vm = repository.createTeamFinance(request, stripeAccountId);
            // TODO: Add account id to users' coach data

            return null;
        } catch (StripeException e) {
            logger.error(e.getMessage());
            return null; // TODO: handle error response
        }
    }

    public ResponseEntity<String> createAccountLink(String stripeAccountId, String returnUrl) {
        String fullReturnUrl = CLIENT_URL + "/" + returnUrl;

        try {
            AccountLinkCreateParams params = AccountLinkCreateParams.builder()
                    .setAccount(stripeAccountId)
                    .setRefreshUrl(fullReturnUrl)
                    .setReturnUrl(fullReturnUrl)
                    .setType(AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING)
                    .build();

            AccountLink accountLink = AccountLink.create(params);
            return new ResponseEntity<>(accountLink.getUrl(), HttpStatus.SEE_OTHER); // TODO: check http status
        } catch (StripeException e) {
            logger.error(e.getMessage());
            return null; // TODO: handle error response
        }
    }
}
