package com.nazlicanguner.careerflow.interview;

public class InterviewStageAlreadyExistsException
        extends RuntimeException {

    public InterviewStageAlreadyExistsException(
            Long jobApplicationId,
            Integer stageNumber
    ) {
        super(
                "Stage " + stageNumber
                        + " already exists for job application with id "
                        + jobApplicationId + "."
        );
    }
}