package com.example.batch.config;

import com.example.batch.listener.JobCompletionListener;
import com.example.batch.model.CustomerOrderProductDTO;
import com.example.batch.reader.SqlFileItemReader;
import com.example.batch.writer.RestApiWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public BatchConfig(JobBuilderFactory jobBuilderFactory,
                       StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job restSubmitJob(JobCompletionListener listener, Step readAndSubmitStep) {
        return jobBuilderFactory.get("restSubmitJob")
                .listener(listener)
                .start(readAndSubmitStep)
                .build();
    }

    @Bean
    public Step readAndSubmitStep(SqlFileItemReader reader, RestApiWriter writer) {
        return stepBuilderFactory.get("readAndSubmitStep")
                .<CustomerOrderProductDTO, CustomerOrderProductDTO>chunk(10)
                .reader(reader)
                .writer(writer)
                .build();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RetryTemplate retryTemplate() {
        return RetryTemplate.builder()
                .maxAttempts(3)
                .fixedBackoff(2000)
                .build();
    }
}
