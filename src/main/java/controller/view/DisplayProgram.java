package controller.view;

import java.util.Date;
import java.util.UUID;

public class DisplayProgram {

    private UUID id;
    private Date createdAt;
    private Date updatedAt;
    private Date startDate;
    private Date endDate;

    public DisplayProgram(UUID id, Date createdAt, Date updatedAt, Date startDate, Date endDate) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public UUID getId() {
        return id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }
}
