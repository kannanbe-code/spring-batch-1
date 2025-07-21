package com.example.batch.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

/**
 * Listener for job lifecycle events.
 * Used to audit and log job execution status, summaries, and metrics.
 *
 * Listener to perform logging and auditing before and after each step execution.
 */
@Slf4j
@Component
public class JobCompletionListener implements JobExecutionListener {

    /**
     * Invoked before job starts.
     *
     * @param jobExecution job context with metadata
     */
    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info(">>> Job started: {}", jobExecution.getJobInstance().getJobName());
    }

    /**
     * Invoked after job completes.
     *
     * @param jobExecution job context containing status, exit info, and metrics
     */
    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info("<<< Job finished. Status: {}, Exit: {}",
                jobExecution.getStatus(), jobExecution.getExitStatus());

        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("✔ SUCCESS: Job completed with {} step(s).", jobExecution.getStepExecutions().size());

            jobExecution.getStepExecutions().forEach(stepExecution -> {
                log.info(" - Step [{}] processed {} items (read: {}, written: {}, skipped: {})",
                        stepExecution.getStepName(),
                        stepExecution.getWriteCount(),
                        stepExecution.getReadCount(),
                        stepExecution.getWriteCount(),
                        stepExecution.getSkipCount());
            });
        } else if (jobExecution.getStatus() == BatchStatus.FAILED) {
            log.error("✖ FAILURE: Job failed with exit code [{}] and message: {}",
                    jobExecution.getExitStatus().getExitCode(),
                    jobExecution.getExitStatus().getExitDescription());

            jobExecution.getAllFailureExceptions().forEach(ex -> {
                log.error(" - Exception: {}", ex.getMessage(), ex);
            });
        }
    }
}
