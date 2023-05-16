package com.coachapp.coach_pc.model.payment;

import com.coachapp.coach_pc.enums.StripeStatus;
import com.coachapp.coach_pc.model.Team;
import com.coachapp.coach_pc.model.user.CoachData;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class TeamFinance {

    @Id
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "coach_id", referencedColumnName = "id")
    private CoachData coach;
    @OneToOne
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    private Team team;
    private String stripeConnectId;
    @Enumerated(EnumType.STRING)
    private StripeStatus stripeStatus;
    private int teamPrice = 0;

    public TeamFinance() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public CoachData getCoach() {
        return coach;
    }

    public void setCoach(CoachData coach) {
        this.coach = coach;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public String getStripeConnectId() {
        return stripeConnectId;
    }

    public void setStripeConnectId(String stripeConnectId) {
        this.stripeConnectId = stripeConnectId;
    }

    public StripeStatus getStripeStatus() {
        return stripeStatus;
    }

    public void setStripeStatus(StripeStatus stripeStatus) {
        this.stripeStatus = stripeStatus;
    }

    public int getTeamPrice() {
        return teamPrice;
    }

    public void setTeamPrice(int teamPrice) {
        this.teamPrice = teamPrice;
    }
}
