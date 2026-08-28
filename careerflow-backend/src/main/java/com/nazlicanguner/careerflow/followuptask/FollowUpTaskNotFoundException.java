package com.nazlicanguner.careerflow.followuptask;

public class FollowUpTaskNotFoundException extends RuntimeException {

    public FollowUpTaskNotFoundException(Long id) {
        super("Follow-up task with id " + id + " was not found.");
    }
}