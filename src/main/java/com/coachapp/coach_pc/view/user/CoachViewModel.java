package com.coachapp.coach_pc.view.user;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.coachapp.coach_pc.model.user.CoachData;

import java.util.UUID;

@EntityView(CoachData.class)
public interface CoachViewModel {

    @IdMapping
    UUID getId();
}
