package com.example.batch.config;

import com.example.batch.listener.JobCompletionListener;
import com.example.batch.model.CustomerOrderProductDTO;
import com.example.batch.reader.SqlFileItemReader;
import com.example.batch.writer.RestApiWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Bean
    public Job restSubmitJob(JobCompletionListener listener, Step readAndSubmitStep) {
        return new JobBuilder("restSubmitJob")
                .listener(listener)
                .start(readAndSubmitStep)
                .build();
    }

    @Bean
    public Step readAndSubmitStep(SqlFileItemReader reader, RestApiWriter writer) {
        return new StepBuilder("readAndSubmitStep")
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
