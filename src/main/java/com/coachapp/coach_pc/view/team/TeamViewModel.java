package com.coachapp.coach_pc.view.team;

import com.blazebit.persistence.view.*;
import com.coachapp.coach_pc.model.team.Team;

import java.util.Date;
import java.util.UUID;

@EntityView(Team.class)
public interface TeamViewModel {

    @IdMapping
    UUID getId();
    @Mapping("name")
    String getName();
    @Mapping("description")
    String getDescription();
    Date getCreatedAt();
    @Mapping("teamLogo")
    String getTeamLogo();
}
