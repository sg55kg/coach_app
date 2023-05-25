package com.coachapp.coach_pc.view.team;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import com.coachapp.coach_pc.model.team.Team;

import java.util.Date;

@UpdatableEntityView
@CreatableEntityView
@EntityView(Team.class)
public interface UpdatableTeamViewModel extends TeamViewModel {

    void setName(String name);
    void setDescription(String description);
    void setCreatedAt(Date createdAt);
    void setTeamLogo(String teamLogo);
}
