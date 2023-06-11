package com.coachapp.coach_pc.service.email;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.SendTemplatedEmailRequest;
import com.coachapp.coach_pc.request.user.InviteUserRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SESService {

    private final AmazonSimpleEmailService mailSender;
    private final String TEMPLATE_NAME = "coachable_user_invite_v8";
    private final String FROM_ADDRESS = "Coachable <no-reply@info.coachablefitness.com>";

    public SESService(AmazonSimpleEmailService mailSender) {
        this.mailSender = mailSender;
    }

    public void sendUserInviteEmail(String url, InviteUserRequest request) {
        Destination destination = new Destination();
        destination.setToAddresses(List.of(request.getAthleteEmail()));

        SendTemplatedEmailRequest templatedEmailRequest = new SendTemplatedEmailRequest();
        templatedEmailRequest.withDestination(destination);
        templatedEmailRequest.withTemplate(TEMPLATE_NAME);
        templatedEmailRequest.withTemplateData(
                "{\"inviter\":\"" +
                request.getCoachName() +
                "\",\"name\":\"" +
                request.getAthleteName() +
                "\",\"invite_url\":\"" +
                url +
                "\",\"team_name\":\"" +
                request.getTeamName() +
                "\"}"
        );
        templatedEmailRequest.withSource(FROM_ADDRESS);

        mailSender.sendTemplatedEmail(templatedEmailRequest);
    }
}
