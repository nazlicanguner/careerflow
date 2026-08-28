package com.nazlicanguner.careerflow.common;

import com.nazlicanguner.careerflow.company.CompanyNotFoundException;
import com.nazlicanguner.careerflow.jobapplication.JobApplicationNotFoundException;
import com.nazlicanguner.careerflow.company.CompanyHasJobApplicationsException;
import com.nazlicanguner.careerflow.interview.InterviewNotFoundException;
import com.nazlicanguner.careerflow.interview.InterviewStageAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CompanyNotFoundException.class)
    public ResponseEntity<ApiError> handleCompanyNotFound(
            CompanyNotFoundException exception
    ) {
        ApiError apiError = new ApiError(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                exception.getMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    @ExceptionHandler(JobApplicationNotFoundException.class)
    public ResponseEntity<ApiError> handleJobApplicationNotFound(
            JobApplicationNotFoundException exception
    ) {
        ApiError apiError = new ApiError(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                exception.getMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    @ExceptionHandler(CompanyHasJobApplicationsException.class)
    public ResponseEntity<ApiError> handleCompanyHasJobApplications(
            CompanyHasJobApplicationsException exception
    ) {
        ApiError apiError = new ApiError(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                exception.getMessage()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
    }

    @ExceptionHandler(InterviewNotFoundException.class)
    public ResponseEntity<ApiError> handleInterviewNotFound(
            InterviewNotFoundException exception
    ) {
        ApiError apiError = new ApiError(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                exception.getMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    @ExceptionHandler(InterviewStageAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleInterviewStageAlreadyExists(
            InterviewStageAlreadyExistsException exception
    ) {
        ApiError apiError = new ApiError(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                exception.getMessage()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
    }
}