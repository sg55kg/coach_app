package controller.request;

import model.Day;

import java.util.List;
import java.util.UUID;

public class ProgramRequest {

    private UUID id;
    private List<Day> days;

    public ProgramRequest() {}

    public ProgramRequest(UUID id, List<Day> days) {
        this.id = id;
        this.days = days;
    }

    public UUID getId() {
        return id;
    }

    public List<Day> getDays() {
        return days;
    }
}
