package com.nazlicanguner.careerflow.activitylog;

public class ActivityLogSummary {

    private String entityType;
    private String action;
    private long count;

    public ActivityLogSummary() {
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}