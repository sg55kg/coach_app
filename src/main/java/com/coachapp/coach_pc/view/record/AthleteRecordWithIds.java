package com.coachapp.coach_pc.view.record;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.FetchStrategy;
import com.blazebit.persistence.view.Mapping;
import com.coachapp.coach_pc.model.user.AthleteRecord;

import java.util.UUID;

@EntityView(AthleteRecord.class)
public interface AthleteRecordWithIds extends AthleteRecordViewModel {

    @Mapping(value = "exercise.id", fetch = FetchStrategy.SELECT)
    UUID getExerciseId();
    @Mapping(value = "day.id", fetch = FetchStrategy.SELECT)
    UUID getDayId();
}
