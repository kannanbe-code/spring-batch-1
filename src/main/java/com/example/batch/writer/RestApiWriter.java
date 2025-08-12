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
    public void write(List<? extends String> items) {
        log.info("Processing {} items for REST API submission", items.size());

        for (String item : items) {
            log.debug("Preparing to send item: {}", item);

            retryTemplate.execute((RetryCallback<ResponseEntity<String>, RuntimeException>) context -> {
                // Create HTTP headers
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set("X-Request-Source", "SpringBatch");
                headers.set("X-Retry-Attempt", String.valueOf(context.getRetryCount() + 1));
                headers.setBearerAuth("your-access-token"); // Replace with dynamic token if needed

                // Wrap the item and headers into HttpEntity
                HttpEntity<String> requestEntity = new HttpEntity<>(item, headers);

                log.info("Attempt #{} sending item to API: {}", context.getRetryCount() + 1, item);

                // POST request with headers
                ResponseEntity<String> response = restTemplate.postForEntity(API_URL, requestEntity, String.class);

                log.info("Response for item {}: HTTP {} - {}", item, response.getStatusCode(), response.getBody());

                return response;
            });
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
