package com.nazlicanguner.careerflow.activitylog;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ActivityLogService {

    private final ActivityLogRepository activityLogRepository;

    public ActivityLogService(ActivityLogRepository activityLogRepository) {
        this.activityLogRepository = activityLogRepository;
    }

    public void log(
            ActivityEntityType entityType,
            Long entityId,
            ActivityAction action,
            String message
    ) {
        ActivityLog activityLog = new ActivityLog();
        activityLog.setEntityType(entityType);
        activityLog.setEntityId(entityId);
        activityLog.setAction(action);
        activityLog.setMessage(message);
        activityLog.setOccurredAt(LocalDateTime.now());

        activityLogRepository.save(activityLog);
    }

    public List<ActivityLog> getAllActivityLogs() {
        return activityLogRepository.findAllByOrderByOccurredAtDesc();
    }
}