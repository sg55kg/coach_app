package com.coachapp.coach_pc.view.user;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.FetchStrategy;
import com.blazebit.persistence.view.Mapping;
import com.coachapp.coach_pc.model.AthleteData;
import com.coachapp.coach_pc.view.program.ProgramWithDays;
import com.coachapp.coach_pc.view.program.ProgramWithIds;

import java.util.List;

@EntityView(AthleteData.class)
public interface AthleteWithPrograms extends AthleteViewModel {

//    @Mapping(value = "currentProgram")
//    ProgramWithDays getCurrentProgram();
//    @Mapping(value = "programsList", fetch = FetchStrategy.SUBSELECT)
//    List<ProgramWithIds> getPrograms();
}
