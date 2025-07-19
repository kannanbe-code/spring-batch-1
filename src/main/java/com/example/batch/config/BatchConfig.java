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
import org.springframework.batch.item.support.IteratorItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Bean
    public Job job(JobBuilderFactory jobs, Step step, JobCompletionListener listener) {
        return jobs.get("restSubmitJob")
                .listener(listener)
                .flow(step)
                .end()
                .build();
    }

    @Bean
    public Step step(StepBuilderFactory steps,
                     SqlFileItemReader reader,
                     RestApiWriter writer) {
        return steps.get("readAndSubmitStep")
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
