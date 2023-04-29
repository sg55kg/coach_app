package com.coachapp.coach_pc.view.user;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.coachapp.coach_pc.model.user.UserPreference;

import java.util.UUID;

@EntityView(UserPreference.class)
public interface UserPreferenceViewModel {
    @IdMapping
    UUID getId();
    String getWeight();
    String getMode();
}
