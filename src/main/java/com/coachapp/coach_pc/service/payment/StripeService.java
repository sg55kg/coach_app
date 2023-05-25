package com.coachapp.coach_pc.service.payment;

import com.coachapp.coach_pc.enums.StripeStatus;
import com.coachapp.coach_pc.repository.payment.PaymentRepository;
import com.coachapp.coach_pc.request.payment.AthletePaymentRequest;
import com.coachapp.coach_pc.request.payment.NewStripeAccountRequest;
import com.coachapp.coach_pc.view.payment.AthletePaymentRecordWithIds;
import com.coachapp.coach_pc.view.payment.TeamFinanceWithIds;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.net.RequestOptions;
import com.stripe.param.AccountCreateParams;
import com.stripe.param.AccountLinkCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
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
    @Value("${stripe-client-id}")
    private String STRIPE_CLIENT_ID;
    @Value("${client-url}")
    private String CLIENT_URL;
    private final Logger logger = LoggerFactory.getLogger(StripeService.class);
    private final PaymentRepository repository;

    public StripeService(PaymentRepository repository) {
        this.repository = repository;
    }

    public ResponseEntity<TeamFinanceWithIds> connectNewStripeAccount(NewStripeAccountRequest request) {
        Stripe.apiKey = STRIPE_API_KEY;
        Stripe.clientId = STRIPE_CLIENT_ID;

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
        Stripe.clientId = STRIPE_CLIENT_ID;
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
        Stripe.clientId = STRIPE_CLIENT_ID;

        try {
            Account account = Account.retrieve(stripeConnectId);
            return new ResponseEntity<>(account.toJson(), HttpStatus.OK);
        } catch (StripeException e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<String> createStripeCheckoutSession(String stripeConnectId, AthletePaymentRequest request) {
        Stripe.apiKey = STRIPE_API_KEY;
        Stripe.clientId = STRIPE_CLIENT_ID;

        try {
            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setPrice("1")
                                    .setQuantity(1L)
                                    .build()
                    )
                    .setPaymentIntentData(
                            SessionCreateParams.PaymentIntentData.builder()
                                    .setApplicationFeeAmount(123L)
                                    .build()
                    )
                    .setSuccessUrl("TODO")
                    .setCancelUrl("TODO")
                    .build();

            RequestOptions requestOptions = RequestOptions.builder().setStripeAccount("TODO").build();

            Session session = Session.create(params, requestOptions);
            String sessionId = session.getId();
            String url = session.getUrl();

            AthletePaymentRecordWithIds paymentRecord = repository.createAthletePaymentRecord(request, sessionId);
            return new ResponseEntity<>(url, HttpStatus.SEE_OTHER);
        } catch (StripeException e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> handleStripeWebhook(Event event) {
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = null;
        if (dataObjectDeserializer.getObject().isPresent()) {
            stripeObject = dataObjectDeserializer.getObject().get();
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        switch (event.getType()) {
            case "checkout.session.async_payment_failed": {
                logger.info("Received async payment failed for account: " + event.getAccount());
                // implement repository method to update payment
                break;
            }
            case "checkout.session.async_payment_succeeded": {
                logger.info("Received async payment succeeded for account: " + event.getAccount());
                // Then define and call a function to handle the event checkout.session.async_payment_succeeded
                break;
            }
            case "checkout.session.completed": {
                logger.info("Received checkout session completed for account: " + event.getAccount());
                // Then define and call a function to handle the event checkout.session.completed
                break;
            }
            case "checkout.session.expired": {
                logger.info("Received checkout session expired for account: " + event.getAccount());
                // Then define and call a function to handle the event checkout.session.expired
                break;
            }
            case "charge.succeeded": {
                logger.info("Received successful charge event for account: " + event.getAccount());
                break;
            }
            case "payment_intent.created": {
                logger.info("Received payment intent creation event for account: " + event.getAccount());
                break;
            }
            case "payment_intent.succeeded": {
                logger.info("Received successful payment intent event for account: " + event.getAccount());
                break;
            }
            default:
                logger.error("Unhandled Stripe event type: " + event.getType());
        }
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
