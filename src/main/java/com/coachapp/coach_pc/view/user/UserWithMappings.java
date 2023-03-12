package com.coachapp.coach_pc.view.user;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.Mapping;
import com.coachapp.coach_pc.model.UserData;
import com.coachapp.coach_pc.view.chat.ChatMemberViewModel;

import java.util.List;

@EntityView(UserData.class)
public interface UserWithMappings extends UserViewModel {

    //@Mapping("athleteData")
    AthleteViewModel getAthleteData();
    //@Mapping("coachData")
    CoachWithMappings getCoachData();
    List<ChatMemberViewModel> getMembers();
}
