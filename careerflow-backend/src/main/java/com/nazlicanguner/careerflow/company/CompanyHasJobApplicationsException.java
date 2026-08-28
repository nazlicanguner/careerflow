package com.nazlicanguner.careerflow.company;

public class CompanyHasJobApplicationsException extends RuntimeException {

    public CompanyHasJobApplicationsException(Long id) {
        super(
                "Company with id " + id
                        + " cannot be deleted because it has job applications."
        );
    }
}