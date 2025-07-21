package com.example.batch.writer;

import com.example.batch.model.CustomerOrderProductDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Custom ItemWriter to send each DTO to a REST API.
 * Uses RetryTemplate for resiliency and logs errors to a local file.
 */
@Component
public class RestApiWriter implements ItemWriter<CustomerOrderProductDTO> {

    private static final Logger logger = LoggerFactory.getLogger(RestApiWriter.class);

    private final RestTemplate restTemplate;
    private final RetryTemplate retryTemplate;

    @Value("${api.endpoint.url}")
    private String apiUrl;

    private static final String FAILED_FILE = "failed-records.txt";

    public RestApiWriter(RestTemplate restTemplate, RetryTemplate retryTemplate) {
        this.restTemplate = restTemplate;
        this.retryTemplate = retryTemplate;
    }

    /**
     * Called for each chunk of items. This method attempts to send each DTO
     * to the REST API, and if it fails after retrying, logs it to a file.
     *
     * @param items chunk of DTOs to submit
     */
    @Override
    public void write(List<? extends CustomerOrderProductDTO> items) {
        for (CustomerOrderProductDTO item : items) {
            try {
                // Use RetryTemplate to handle transient errors like timeouts
                retryTemplate.execute(context -> {
                    logger.debug("Submitting to API: {}", item);
                    restTemplate.postForEntity(apiUrl, item, String.class);
                    return null;
                });
                logger.info("Successfully submitted: {}", item.getOrderId());

            } catch (RestClientException e) {
                logger.error("Failed to submit item after retries: {}", item, e);
                writeFailedItemToFile(item);
            }
        }
    }

    /**
     * Writes a failed item to a local file for audit or reprocessing.
     *
     * @param item DTO that failed to submit
     */
    private void writeFailedItemToFile(CustomerOrderProductDTO item) {
        try (FileWriter writer = new FileWriter(FAILED_FILE, true)) {
            writer.write(item.toString() + System.lineSeparator());
        } catch (IOException e) {
            logger.error("Failed to write item to failure log: {}", item, e);
        }
    }
}
