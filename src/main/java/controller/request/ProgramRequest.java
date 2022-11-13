package controller.request;

import model.Day;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ProgramRequest {

    private UUID id;
    private Set<Day> days;

    public ProgramRequest() {}

    public ProgramRequest(UUID id, Set<Day> days) {
        this.id = id;
        this.days = days;
    }

    public UUID getId() {
        return id;
    }

    public Set<Day> getDays() {
        return days;
    }
}
