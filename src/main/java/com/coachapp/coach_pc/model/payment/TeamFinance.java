package com.coachapp.coach_pc.model.payment;

import com.coachapp.coach_pc.enums.StripeStatus;
import com.coachapp.coach_pc.model.team.Team;
import com.coachapp.coach_pc.model.user.CoachData;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class TeamFinance {

    // recommend using lombok @Getter and @Setter for your fields
    // You can annotate any field with @Getter and/or @Setter, to let lombok generate the default getter/setter automatically.
    // https://projectlombok.org/features/GetterSetter
    // makes it more readable and less prone to errors

    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
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
    private int athleteCap = -1;
    private boolean requestRequired = false;
    private boolean inviteOnly = false;

    // In Java, if you don't explicitly define any constructor in a class,
    // the compiler automatically provides
    // a default constructor with no arguments (also known as a no-arg constructor).
    // so this is not needed
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

    public int getAthleteCap() {
        return athleteCap;
    }

    public void setAthleteCap(int athleteCap) {
        this.athleteCap = athleteCap;
    }

    public boolean getRequestRequired() {
        return requestRequired;
    }

    public void setRequestRequired(boolean requestRequired) {
        this.requestRequired = requestRequired;
    }

    public boolean getInviteOnly() {
        return inviteOnly;
    }

    public void setInviteOnly(boolean inviteOnly) {
        this.inviteOnly = inviteOnly;
    }
}
