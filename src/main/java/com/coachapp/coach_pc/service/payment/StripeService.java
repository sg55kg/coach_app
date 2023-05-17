package com.coachapp.coach_pc.service.payment;

import com.coachapp.coach_pc.enums.StripeStatus;
import com.coachapp.coach_pc.repository.PaymentRepository;
import com.coachapp.coach_pc.request.payment.NewStripeAccountRequest;
import com.coachapp.coach_pc.view.payment.TeamFinanceViewModel;
import com.coachapp.coach_pc.view.payment.TeamFinanceWithIds;
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

    public ResponseEntity<TeamFinanceWithIds> connectNewStripAccount(NewStripeAccountRequest request) {
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
            String stripeConnectId = account.getId();
            TeamFinanceWithIds vm = repository.createTeamFinance(request, stripeConnectId);

            return new ResponseEntity<>(vm, HttpStatus.CREATED);
        } catch (StripeException e) {
            logger.error(e.getMessage());
            return null; // TODO: handle error response
        }
    }

    public ResponseEntity<String> createAccountLink(String stripeConnectId, String returnUrl) {
        Stripe.apiKey = STRIPE_API_KEY;
        String fullReturnUrl = CLIENT_URL + returnUrl;

        try {
            AccountLinkCreateParams params = AccountLinkCreateParams.builder()
                    .setAccount(stripeConnectId)
                    .setRefreshUrl(fullReturnUrl)
                    .setReturnUrl(fullReturnUrl)
                    .setType(AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING)
                    .build();

            AccountLink accountLink = AccountLink.create(params);
            repository.updateTeamFinanceStripeStatus(StripeStatus.ONBOARDING, stripeConnectId);

            return new ResponseEntity<>(accountLink.getUrl(), HttpStatus.SEE_OTHER);
        } catch (StripeException e) {
            logger.error(e.getMessage());
            return null; // TODO: handle error response
        }
    }

    public ResponseEntity<String> getStripeAccount(String stripeConnectId) {
        Stripe.apiKey = STRIPE_API_KEY;

        try {
            Account account = Account.retrieve(stripeConnectId);
            return new ResponseEntity<>(account.toJson(), HttpStatus.OK);
        } catch (StripeException e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
