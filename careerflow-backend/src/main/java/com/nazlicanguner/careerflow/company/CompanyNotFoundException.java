package com.nazlicanguner.careerflow.company;

public class CompanyNotFoundException extends RuntimeException {

    public CompanyNotFoundException(Long id) {
        super("Company with id " + id + " was not found.");
    }
}