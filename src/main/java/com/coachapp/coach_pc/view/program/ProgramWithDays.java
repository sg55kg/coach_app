package com.coachapp.coach_pc.view.program;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.Mapping;
import com.coachapp.coach_pc.model.program.Program;

import java.util.List;

@EntityView(Program.class)
public interface ProgramWithDays extends ProgramWithIds {

    @Mapping("daysList")
    List<DayViewModel> getDays();
}
