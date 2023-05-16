package com.coachapp.coach_pc.view.team;

import com.blazebit.persistence.view.*;
import com.coachapp.coach_pc.model.Team;
import com.coachapp.coach_pc.view.AthleteViewModel;

import java.util.Date;
import java.util.List;
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
