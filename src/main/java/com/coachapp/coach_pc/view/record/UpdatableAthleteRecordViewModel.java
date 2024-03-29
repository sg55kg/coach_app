package com.coachapp.coach_pc.view.record;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import com.coachapp.coach_pc.model.user.AthleteRecord;

@UpdatableEntityView
@EntityView(AthleteRecord.class)
public interface UpdatableAthleteRecordViewModel extends AthleteRecordViewModel {

    void setIsCurrent(boolean isCurrent);
}
