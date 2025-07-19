package com.example.batch;

import com.example.batch.config.BatchConfig;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BatchApplicationTests {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job restSubmitJob;

    @Test
    void testJobExecution() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters();

        JobExecution jobExecution = jobLauncher.run(restSubmitJob, jobParameters);

        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    }
}
