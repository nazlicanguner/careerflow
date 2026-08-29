package com.nazlicanguner.careerflow.activitylog;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "activity_logs")
public class ActivityLog {

    @Id
    private String id;

    @Indexed
    private ActivityEntityType entityType;

    @Indexed
    private Long entityId;

    private ActivityAction action;

    private String message;

    @Indexed
    private LocalDateTime occurredAt;

    public String getId() {
        return id;
    }

    public ActivityEntityType getEntityType() {
        return entityType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public ActivityAction getAction() {
        return action;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }

    public void setEntityType(ActivityEntityType entityType) {
        this.entityType = entityType;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public void setAction(ActivityAction action) {
        this.action = action;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setOccurredAt(LocalDateTime occurredAt) {
        this.occurredAt = occurredAt;
    }
}