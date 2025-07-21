package com.example.batch.writer;

import com.example.batch.model.CustomerOrderProductDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
 *
 * Writer class responsible for submitting CustomerOrderProductDTO items
 * to an external REST API endpoint in JSON format.
 *
 * It supports retry using RetryTemplate and logs all outcomes.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RestApiWriter implements ItemWriter<CustomerOrderProductDTO> {

    private final RestTemplate restTemplate;
    private final RetryTemplate retryTemplate;

    // REST endpoint URL to which the data will be posted
    @Value("${app.rest.endpoint}")
    private String endpointUrl;

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
                    log.debug("Submitting record to API: {}", item);
                    restTemplate.postForEntity(apiUrl, item, String.class);
                    return null;
                });
                logger.info("Successfully submitted item: {}", item.getOrderId());

            } catch (RestClientException e) {
                log.error("Failed to post item {} after retries. Error: {}", item.getOrderId(), e.getMessage(), e);
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
