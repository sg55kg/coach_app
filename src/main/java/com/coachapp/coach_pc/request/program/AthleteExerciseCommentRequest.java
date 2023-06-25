package com.coachapp.coach_pc.request.program;

import com.coachapp.coach_pc.model.exercise.AthleteExerciseComment;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class AthleteExerciseCommentRequest {

    // recommend using lombok @Getter and @Setter for your fields
    // You can annotate any field with @Getter and/or @Setter, to let lombok generate the default getter/setter automatically.
    // https://projectlombok.org/features/GetterSetter
    // makes it more readable and less prone to errors

    private UUID id;
    @JsonAlias("athleteId")
    @JsonProperty("athleteId")
    private UUID athleteId;
    @JsonAlias("exerciseId")
    @JsonProperty("exerciseId")
    private UUID exerciseId;
    private String content;
    private String commenterName;

    public AthleteExerciseCommentRequest() {
    }

    public AthleteExerciseCommentRequest(UUID id, UUID athleteId, UUID exerciseId, String content, String commenterName) {
        this.id = id;
        this.athleteId = athleteId;
        this.exerciseId = exerciseId;
        this.content = content;
        this.commenterName = commenterName;
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

    public String getCommenterName() {
        return commenterName;
    }

    public static AthleteExerciseComment convertRequest(AthleteExerciseCommentRequest request) {
        AthleteExerciseComment comment = new AthleteExerciseComment();

        // remove unused code
        //Exercise exercise = new Exercise();
       //exercise.setId(request.getExerciseId());

        if (request.getId() != null) {
            comment.setId(request.getId());
        }

        comment.setAthleteId(request.getAthleteId());
        //comment.setExercise(exercise);
        comment.setContent(request.getContent());
        comment.setCommenterName(request.getCommenterName());

        return comment;
    }
}
