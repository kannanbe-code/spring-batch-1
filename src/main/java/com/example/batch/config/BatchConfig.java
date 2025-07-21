package com.example.batch.config;

import com.example.batch.listener.JobCompletionListener;
import com.example.batch.model.CustomerOrderProductDTO;
import com.example.batch.reader.SqlFileItemReader;
import com.example.batch.writer.RestApiWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.client.RestTemplate;

/**
 * Spring Batch configuration for defining jobs and steps.
 * This class is fully compatible with Spring Batch 5.x and avoids deprecated APIs.
 */
@Configuration
public class BatchConfig {

    private static final Logger logger = LoggerFactory.getLogger(BatchConfig.class);

    /**
     * Defines the batch job that includes a single step.
     *
     * @param jobRepository the JobRepository required by Spring Batch 5
     * @param listener      job-level listener for auditing
     * @param readAndSubmitStep the main step that reads data and submits to REST API
     * @return configured Job
     */
    @Bean
    public Job restSubmitJob(JobRepository jobRepository,
                              JobCompletionListener listener,
                              Step readAndSubmitStep) {
        logger.info("Creating Job: restSubmitJob");

        return new JobBuilder("restSubmitJob", jobRepository)
                .listener(listener)         // Listener for logging and auditing
                .start(readAndSubmitStep)   // Single step job
                .build();
    }

    /**
     * Defines the step to read data from SQL file and write to REST API.
     *
     * @param jobRepository Spring Batch 5.x JobRepository
     * @param transactionManager manages transaction for chunk processing
     * @param reader custom reader that executes a SQL file
     * @param writer custom writer that submits data to REST API
     * @return configured Step
     */
    @Bean
    public Step readAndSubmitStep(JobRepository jobRepository,
                                  PlatformTransactionManager transactionManager,
                                  SqlFileItemReader reader,
                                  RestApiWriter writer) {
        logger.info("Creating Step: readAndSubmitStep");

        return new StepBuilder("readAndSubmitStep", jobRepository)
                .<CustomerOrderProductDTO, CustomerOrderProductDTO>chunk(10, transactionManager)
                .reader(reader)
                .writer(writer)
                .build();
    }

    /**
     * Retry configuration for handling temporary REST API failures.
     *
     * @return RetryTemplate with max 3 attempts and 2s delay
     */
    @Bean
    public RetryTemplate retryTemplate() {
        return RetryTemplate.builder()
                .maxAttempts(3)        // Retry up to 3 times
                .fixedBackoff(2000)    // Wait 2 seconds between retries
                .build();
    }

    /**
     * Standard RestTemplate for HTTP POST requests.
     *
     * @return configured RestTemplate
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
