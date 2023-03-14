package com.coachapp.coach_pc.view.program;

import com.blazebit.persistence.SubqueryInitiator;
import com.blazebit.persistence.view.*;
import com.coachapp.coach_pc.model.Day;
//import com.coachapp.coach_pc.model.Program;
//
//import java.time.OffsetDateTime;
//
//@EntityView(Program.class)
//public interface DisplayProgram extends ProgramWithIds {
//
////    @MappingSubquery()
////    OffsetDateTime getLastEnteredDay();
////
////    class DisplayProgramSubqueryProvider implements SubqueryProvider {
////
////        @Override
////        public <T> T createSubquery(SubqueryInitiator<T> subqueryBuilder) {
////            return subqueryBuilder.from(Day.class, "d")
////                    .select("date")
////                    .where("d.exercises.length").
////                    .where(d.isR)
////        }
////    }
//}
