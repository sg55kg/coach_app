package com.coachapp.coach_pc.view.user;

import com.blazebit.persistence.view.EntityView;
import com.coachapp.coach_pc.model.user.AthleteData;

@EntityView(AthleteData.class)
public interface AthleteWithPrograms extends AthleteViewModel {

//    @Mapping(value = "currentProgram")
//    ProgramWithDays getCurrentProgram();
//    @Mapping(value = "programsList", fetch = FetchStrategy.SUBSELECT)
//    List<ProgramWithIds> getPrograms();
}
