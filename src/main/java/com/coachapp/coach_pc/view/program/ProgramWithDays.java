package com.coachapp.coach_pc.view.program;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.Mapping;
import com.coachapp.coach_pc.model.Day;
import com.coachapp.coach_pc.model.Program;

import java.util.List;
import java.util.UUID;

@EntityView(Program.class)
public interface ProgramWithDays extends ProgramWithIds {

    @Mapping("daysList")
    List<DayViewModel> getDays();
}
