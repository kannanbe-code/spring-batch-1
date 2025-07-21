package com.example.batch.config;

import com.example.batch.job.RestApiWriter;
import com.example.batch.job.SqlFileItemReader;
import com.example.batch.listener.CustomStepExecutionListener;
import com.example.batch.model.CustomerOrderProductDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
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
 * - Chunk-oriented processing using CustomerOrderProductDTO
 * - Retry mechanism for REST calls
 * - Custom SQL file reader
 * - Step listener for auditing
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    private final SqlFileItemReader sqlFileItemReader;
    private final RestApiWriter restApiWriter;

     /**
     * Defines the main Spring Batch job.
     * 
     * - Uses an incrementer to allow multiple runs
     * - Starts with the single step
     * - Executes the below step
     * - Uses RunIdIncrementer to avoid job instance collision
     */
     @Bean
    public Job customerOrderJob() {
        return new JobBuilder("customerOrderJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(customerOrderStep())
                .build();
    }

    /**
     * Defines the batch step:
     * - Reads from SQL file
     * - Processes data
     * - Writes to REST endpoint
     * - Includes retry and step listener
     */
    @Bean
    public Step customerOrderStep() {
        return new StepBuilder("customerOrderStep", jobRepository)
                .<CustomerOrderProductDTO, CustomerOrderProductDTO>chunk(10, transactionManager)
                .reader(sqlFileItemReader)
                .processor(itemProcessor())
                .writer(restApiWriter)
                .listener(new CustomStepExecutionListener())
                .build();
    }

     /**
     * Optional processor that could be used for transformation or filtering.
     * In this case, it just returns the input as-is.
     */
    @Bean
    public ItemProcessor<CustomerOrderProductDTO, CustomerOrderProductDTO> itemProcessor() {
        return item -> {
            // Add validation or transformation logic here if needed
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
