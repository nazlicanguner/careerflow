package com.nazlicanguner.careerflow.interview;

public class InterviewNotFoundException extends RuntimeException {

    public InterviewNotFoundException(Long id) {
        super("Interview with id " + id + " was not found.");
    }
}