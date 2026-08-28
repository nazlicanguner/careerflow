package com.nazlicanguner.careerflow.interview;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/interviews")
public class InterviewController {

    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @GetMapping
    public List<Interview> getAllInterviews() {
        return interviewService.getAllInterviews();
    }

    @GetMapping("/{id}")
    public Interview getInterviewById(
            @PathVariable("id") Long id
    ) {
        return interviewService.getInterviewById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Interview createInterview(
            @RequestParam Long jobApplicationId,
            @Valid @RequestBody Interview interview
    ) {
        return interviewService.createInterview(
                jobApplicationId,
                interview
        );
    }

    @PutMapping("/{id}")
    public Interview updateInterview(
            @PathVariable("id") Long id,
            @Valid @RequestBody Interview interview
    ) {
        return interviewService.updateInterview(id, interview);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInterview(@PathVariable("id") Long id) {
        interviewService.deleteInterview(id);
    }
}
