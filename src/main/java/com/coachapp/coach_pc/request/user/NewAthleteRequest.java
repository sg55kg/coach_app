package com.coachapp.coach_pc.request.user;

import com.coachapp.coach_pc.model.user.AthleteRecord;

import java.util.List;
import java.util.UUID;

public class NewAthleteRequest {

    // recommend using lombok @Getter and @Setter for your fields
    // You can annotate any field with @Getter and/or @Setter, to let lombok generate the default getter/setter automatically.
    // https://projectlombok.org/features/GetterSetter
    // makes it more readable and less prone to errors

    private UUID userId;
    private String name;
    private List<AthleteRecord> records;


    public NewAthleteRequest() {

    }

    public NewAthleteRequest(UUID userId, String name, List<AthleteRecord> records) {
        this.userId = userId;
        this.name = name;
        this.records = records;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public List<AthleteRecord> getRecords() {
        return records;
    }
}
