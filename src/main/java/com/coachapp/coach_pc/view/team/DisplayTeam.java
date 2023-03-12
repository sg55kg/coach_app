package com.coachapp.coach_pc.view.team;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.Mapping;
import com.coachapp.coach_pc.model.Team;
import com.coachapp.coach_pc.view.user.AthleteViewModel;

import java.util.List;
import java.util.UUID;

@EntityView(Team.class)
public interface DisplayTeam extends TeamViewModel {

//    @Mapping("COUNT(athletes)")
//    Long getNumAthletes();
    List<AthleteViewModel> getAthletes();
//    @Mapping("coach.user.username")
//    String getCoachName();
    @Mapping("coach.id")
    UUID getCoachId();
}
