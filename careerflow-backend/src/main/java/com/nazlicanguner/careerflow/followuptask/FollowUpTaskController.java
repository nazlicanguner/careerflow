package com.nazlicanguner.careerflow.followuptask;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Follow-Up Tasks", description = "Manage follow-up tasks.")
@RestController
@RequestMapping("/api/follow-up-tasks")
public class FollowUpTaskController {

    private final FollowUpTaskService followUpTaskService;

    public FollowUpTaskController(FollowUpTaskService followUpTaskService) {
        this.followUpTaskService = followUpTaskService;
    }

    @GetMapping
    public List<FollowUpTask> getAllFollowUpTasks() {
        return followUpTaskService.getAllFollowUpTasks();
    }

    @GetMapping("/{id}")
    public FollowUpTask getFollowUpTaskById(
            @PathVariable("id") Long id
    ) {
        return followUpTaskService.getFollowUpTaskById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FollowUpTask createFollowUpTask(
            @RequestParam Long jobApplicationId,
            @Valid @RequestBody FollowUpTask followUpTask
    ) {
        return followUpTaskService.createFollowUpTask(
                jobApplicationId,
                followUpTask
        );
    }

    @PutMapping("/{id}")
    public FollowUpTask updateFollowUpTask(
            @PathVariable("id") Long id,
            @Valid @RequestBody FollowUpTask followUpTask
    ) {
        return followUpTaskService.updateFollowUpTask(id, followUpTask);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFollowUpTask(@PathVariable("id") Long id) {
        followUpTaskService.deleteFollowUpTask(id);
    }
}