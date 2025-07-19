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

@Configuration
public class BatchConfig {

    private static final Logger logger = LoggerFactory.getLogger(BatchConfig.class);

    @Bean
    public Job restSubmitJob(JobRepository jobRepository,
                              JobCompletionListener listener,
                              Step readAndSubmitStep) {
        logger.info("Creating Job: restSubmitJob");

        return new JobBuilder("restSubmitJob", jobRepository)
                .listener(listener)
                .start(readAndSubmitStep)
                .build();
    }

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

    @Bean
    public RetryTemplate retryTemplate() {
        return RetryTemplate.builder()
                .maxAttempts(3)
                .fixedBackoff(2000)
                .build();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
