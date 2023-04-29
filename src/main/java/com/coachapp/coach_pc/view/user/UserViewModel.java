package com.coachapp.coach_pc.view.user;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import com.coachapp.coach_pc.model.user.UserData;

import java.util.Date;
import java.util.UUID;

@EntityView(UserData.class)
public interface UserViewModel {

    @IdMapping
    UUID getId();
    @Mapping("createdAt")
    Date getCreatedAt();
    @Mapping("updatedAt")
    Date getUpdatedAt();
    @Mapping("email")
    String getEmail();
    @Mapping("username")
    String getUsername();
    @Mapping("photoUrl")
    String getPhotoUrl();
    @Mapping("preferences")
    UserPreferenceViewModel getPreferences();
}
