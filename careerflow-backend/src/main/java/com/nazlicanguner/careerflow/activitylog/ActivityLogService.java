package com.nazlicanguner.careerflow.activitylog;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ActivityLogService {

    private final ActivityLogRepository activityLogRepository;
    private final MongoTemplate mongoTemplate;

    public ActivityLogService(
            ActivityLogRepository activityLogRepository,
            MongoTemplate mongoTemplate
    ) {
        this.activityLogRepository = activityLogRepository;
        this.mongoTemplate = mongoTemplate;
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

    public List<ActivityLogSummary> getActivityLogSummary() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("entityType", "action")
                        .count()
                        .as("count"),
                Aggregation.project("count")
                        .and("_id.entityType")
                        .as("entityType")
                        .and("_id.action")
                        .as("action"),
                Aggregation.sort(Sort.Direction.ASC, "entityType", "action")
        );

        return mongoTemplate.aggregate(
                aggregation,
                "activity_logs",
                ActivityLogSummary.class
        ).getMappedResults();
    }
}