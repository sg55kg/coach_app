package com.coachapp.coach_pc.view.program;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import com.coachapp.coach_pc.model.Program;
import com.coachapp.coach_pc.view.DayViewModel;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@EntityView(Program.class)
public interface ProgramViewModel {

    @IdMapping
    UUID getId();
    @Mapping("name")
    String getName();
    @Mapping("startDate")
    OffsetDateTime getStartDate();
    @Mapping("endDate")
    OffsetDateTime getEndDate();
    @Mapping("updatedAt")
    OffsetDateTime getUpdatedAt();
}
