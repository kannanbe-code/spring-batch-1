package com.example.batch.config;

import com.example.batch.job.RestApiWriter;
import com.example.batch.job.SqlFileItemReader;
import com.example.batch.listener.CustomStepExecutionListener;
import com.example.batch.model.CustomerRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.builder.JobBuilderHelper;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Spring Batch configuration class that defines:
 * - Job and Step
 * - Custom reader, processor, writer
 * - Retry and listener logic
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    private final SqlFileItemReader itemReader;
    private final RestApiWriter itemWriter;

     /**
     * Defines the main Spring Batch job.
     * 
     * - Uses an incrementer to allow multiple runs
     * - Starts with the single step
     */
    @Bean
    public Job customerJob() {
        return new JobBuilder("customerJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(customerStep())
                .build();
    }

    /**
     * Defines the main Spring Batch step.
     * 
     * - Uses chunk-based processing
     * - Applies reader, processor, writer
     * - Attaches custom listener
     */
    @Bean
    public Step customerStep() {
        return new StepBuilder("customerStep", jobRepository)
                .<CustomerRecord, CustomerRecord>chunk(10, transactionManager)
                .reader(itemReader)
                .processor(itemProcessor())
                .writer(itemWriter)
                .listener(new CustomStepExecutionListener())
                .build();
    }

     /**
     * Defines the processor to apply transformations or validations.
     * 
     * @return ItemProcessor instance for CustomerRecord
     */
    @Bean
    public ItemProcessor<CustomerRecord, CustomerRecord> itemProcessor() {
        return item -> {
            // You can add filtering or transformation here if needed
            return item;
        };
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
