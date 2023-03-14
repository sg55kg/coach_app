package com.coachapp.coach_pc.view.program;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.coachapp.coach_pc.model.WarmUp;

import java.util.UUID;

@EntityView(WarmUp.class)
public interface WarmUpViewModel {

    @IdMapping
    UUID getId();
    String getInstructions();
}
