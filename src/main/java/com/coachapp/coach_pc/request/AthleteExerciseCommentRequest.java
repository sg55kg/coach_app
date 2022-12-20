package com.coachapp.coach_pc.request;

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
}
