package com.example.batch.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Scheduler that launches the batch job at fixed intervals.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SchedulerConfig {

    private final JobLauncher jobLauncher;
    private final Job customerJob;

    /**
     * Launches the job every 1 minute (can be changed as needed).
     */
    @Scheduled(fixedRate = 60000) // 60 seconds
    public void runJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addDate("startTime", new Date())
                    .toJobParameters();

            log.info("Launching job with parameters: {}", jobParameters);
            JobExecution execution = jobLauncher.run(customerJob, jobParameters);
            log.info("Job status: {}", execution.getStatus());

        } catch (Exception e) {
            log.error("Job execution failed", e);
        }
    }
}
