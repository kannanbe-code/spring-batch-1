package com.example.batch.writer;

import com.example.batch.model.CustomerOrderProductDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestApiWriter implements ItemWriter<CustomerOrderProductDTO> {

    private final RestTemplate restTemplate;
    private final RetryTemplate retryTemplate;

    @Override
    public void write(List<? extends CustomerOrderProductDTO> items) {
        log.info("Writing chunk of {} items to REST API", items.size());

        retryTemplate.execute(context -> {
            restTemplate.postForEntity("http://localhost:8081/api/process", items, String.class);
            return null;
        });
    }
}
