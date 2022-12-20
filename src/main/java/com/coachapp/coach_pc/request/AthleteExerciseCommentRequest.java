package com.coachapp.coach_pc.request;

import com.coachapp.coach_pc.model.AthleteData;
import com.coachapp.coach_pc.model.AthleteExerciseComment;
import com.coachapp.coach_pc.model.Exercise;

import java.util.UUID;

public class AthleteExerciseCommentRequest {

    private UUID id;
    private UUID athleteId;
    private UUID exerciseId;
    private String content;

    public AthleteExerciseCommentRequest() {
    }

    public AthleteExerciseCommentRequest(UUID id, UUID athleteId, UUID exerciseId, String content) {
        this.id = id;
        this.athleteId = athleteId;
        this.exerciseId = exerciseId;
        this.content = content;
    }

    public UUID getId() {
        return id;
    }

    public UUID getAthleteId() {
        return athleteId;
    }

    public UUID getExerciseId() {
        return exerciseId;
    }

    public String getContent() {
        return content;
    }

    public static AthleteExerciseComment convertRequest(AthleteExerciseCommentRequest request) {
        AthleteExerciseComment comment = new AthleteExerciseComment();
        AthleteData athlete = new AthleteData();
        Exercise exercise = new Exercise();
        exercise.setId(request.getExerciseId());
        athlete.setId(request.getAthleteId());

        if (request.getId() != null) {
            comment.setId(request.getId());
        }

        comment.setAthlete(athlete);
        comment.setExercise(exercise);
        comment.setContent(request.getContent());

        return comment;
    }
}