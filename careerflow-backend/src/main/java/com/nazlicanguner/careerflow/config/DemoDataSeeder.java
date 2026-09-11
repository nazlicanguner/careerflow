package com.nazlicanguner.careerflow.config;

import com.nazlicanguner.careerflow.activitylog.ActivityAction;
import com.nazlicanguner.careerflow.activitylog.ActivityEntityType;
import com.nazlicanguner.careerflow.activitylog.ActivityLog;
import com.nazlicanguner.careerflow.activitylog.ActivityLogRepository;
import com.nazlicanguner.careerflow.company.Company;
import com.nazlicanguner.careerflow.company.CompanyRepository;
import com.nazlicanguner.careerflow.followuptask.FollowUpTask;
import com.nazlicanguner.careerflow.followuptask.FollowUpTaskRepository;
import com.nazlicanguner.careerflow.followuptask.TaskStatus;
import com.nazlicanguner.careerflow.interview.Interview;
import com.nazlicanguner.careerflow.interview.InterviewOutcome;
import com.nazlicanguner.careerflow.interview.InterviewRepository;
import com.nazlicanguner.careerflow.interview.InterviewType;
import com.nazlicanguner.careerflow.jobapplication.ApplicationStatus;
import com.nazlicanguner.careerflow.jobapplication.JobApplication;
import com.nazlicanguner.careerflow.jobapplication.JobApplicationRepository;
import com.nazlicanguner.careerflow.jobapplication.WorkMode;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DemoDataSeeder implements CommandLineRunner {

    private final CompanyRepository companyRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final InterviewRepository interviewRepository;
    private final FollowUpTaskRepository followUpTaskRepository;
    private final ActivityLogRepository activityLogRepository;

    public DemoDataSeeder(
            CompanyRepository companyRepository,
            JobApplicationRepository jobApplicationRepository,
            InterviewRepository interviewRepository,
            FollowUpTaskRepository followUpTaskRepository,
            ActivityLogRepository activityLogRepository
    ) {
        this.companyRepository = companyRepository;
        this.jobApplicationRepository = jobApplicationRepository;
        this.interviewRepository = interviewRepository;
        this.followUpTaskRepository = followUpTaskRepository;
        this.activityLogRepository = activityLogRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (companyRepository.count() >= 100) {
            System.out.println("Demo seed data already exists. Skipping seeding.");
            return;
        }

        List<String> companyNames = List.of(
                "Zalando", "N26", "HelloFresh", "Delivery Hero", "Personio",
                "Contentful", "GetYourGuide", "Trade Republic", "SumUp", "Mambu",
                "Flix", "Auto1 Group", "Babbel", "DeepL", "SoundCloud",
                "Klarna", "Spotify", "Celonis", "SAP", "Siemens",
                "Bosch", "Infineon", "Deutsche Telekom", "BMW Group", "Mercedes-Benz",
                "Allianz", "DHL", "Bayer", "BASF", "Henkel",
                "E.ON", "RWE", "Fresenius", "Otto Group", "TUI",
                "Scout24", "About You", "FreeNow", "Bolt", "Mollie",
                "Adyen", "Miro", "Typeform", "GitLab", "JetBrains",
                "HubSpot", "Shopify", "Atlassian", "Cloudflare", "Datadog"
        );

        List<String> industries = List.of(
                "Software", "Fintech", "E-commerce", "Mobility", "Health technology",
                "Cloud services", "Artificial intelligence", "Telecommunications"
        );

        List<String> cities = List.of(
                "Berlin", "Munich", "Hamburg", "Cologne", "Amsterdam",
                "Dublin", "London", "Remote"
        );

        List<String> positions = List.of(
                "Backend Developer", "Frontend Developer", "Full Stack Developer",
                "Java Software Engineer", "Cloud Engineer", "DevOps Engineer",
                "Software Engineer", "Platform Engineer", "Data Engineer",
                "QA Automation Engineer", "Working Student Software Development",
                "Junior Backend Engineer"
        );

        List<String> sources = List.of(
                "LinkedIn", "StepStone", "Company careers page",
                "Indeed", "Referral", "Startup Insider"
        );

        List<Company> companies = new ArrayList<>();

        for (int i = 0; i < 120; i++) {
            Company company = new Company();
            String baseName = companyNames.get(i % companyNames.size());
            String city = cities.get(i % cities.size());

            company.setName(baseName + " " + city + " Office");
            company.setIndustry(industries.get(i % industries.size()));
            company.setLocation(city);
            company.setWebsite("https://www.example.com/company-" + (i + 1));
            company.setNotes("Realistic demo company record generated for the CareerFlow database assessment.");

            companies.add(company);
        }

        List<Company> savedCompanies = companyRepository.saveAll(companies);

        List<JobApplication> applications = new ArrayList<>();

        for (int i = 0; i < 120; i++) {
            JobApplication application = new JobApplication();

            application.setCompany(savedCompanies.get(i));
            application.setPositionTitle(positions.get(i % positions.size()));
            application.setJobUrl("https://jobs.example.com/role-" + (i + 1));
            application.setLocation(cities.get(i % cities.size()));
            application.setStatus(ApplicationStatus.values()[i % ApplicationStatus.values().length]);
            application.setWorkMode(WorkMode.values()[i % WorkMode.values().length]);
            application.setSource(sources.get(i % sources.size()));
            application.setApplicationDate(LocalDate.now().minusDays(150L - i));
            application.setNotes("Seeded job application for demonstrating filtering, sorting and dashboard aggregation.");

            applications.add(application);
        }

        List<JobApplication> savedApplications = jobApplicationRepository.saveAll(applications);

        List<Interview> interviews = new ArrayList<>();

        for (int i = 0; i < 110; i++) {
            Interview interview = new Interview();

            interview.setJobApplication(savedApplications.get(i));
            interview.setStageNumber(1);
            interview.setStageName(
                    List.of("Recruiter call", "Technical interview", "Hiring manager interview", "Final interview")
                            .get(i % 4)
            );
            interview.setScheduledAt(LocalDateTime.now().minusDays(60).plusDays(i));
            interview.setInterviewType(InterviewType.values()[i % InterviewType.values().length]);
            interview.setOutcome(InterviewOutcome.values()[i % InterviewOutcome.values().length]);
            interview.setNotes("Seeded interview record for CareerFlow demonstration data.");

            interviews.add(interview);
        }

        interviewRepository.saveAll(interviews);

        List<FollowUpTask> tasks = new ArrayList<>();

        for (int i = 0; i < 130; i++) {
            FollowUpTask task = new FollowUpTask();

            task.setJobApplication(savedApplications.get(i % savedApplications.size()));
            task.setTitle(
                    List.of(
                            "Send follow-up email",
                            "Prepare interview notes",
                            "Research the company",
                            "Update application status",
                            "Send portfolio link"
                    ).get(i % 5)
            );
            task.setDueDate(LocalDate.now().minusDays(20).plusDays(i));
            task.setStatus(TaskStatus.values()[i % TaskStatus.values().length]);
            task.setNotes("Seeded follow-up task used for task status and overdue filtering.");

            tasks.add(task);
        }

        followUpTaskRepository.saveAll(tasks);

        List<ActivityLog> logs = new ArrayList<>();

        for (int i = 0; i < 240; i++) {
            ActivityLog log = new ActivityLog();

            ActivityEntityType entityType =
                    ActivityEntityType.values()[i % ActivityEntityType.values().length];
            ActivityAction action =
                    ActivityAction.values()[i % ActivityAction.values().length];

            log.setEntityType(entityType);
            log.setEntityId(savedApplications.get(i % savedApplications.size()).getId());
            log.setAction(action);
            log.setMessage(
                    "Seeded " + action + " activity for " + entityType + " record " + (i + 1) + "."
            );
            log.setOccurredAt(LocalDateTime.now().minusHours(i * 3L));

            logs.add(log);
        }

        activityLogRepository.saveAll(logs);

        System.out.println(
                "CareerFlow demo data seeded: 120 companies, 120 applications, "
                        + "110 interviews, 130 follow-up tasks and 240 activity logs."
        );
    }
}