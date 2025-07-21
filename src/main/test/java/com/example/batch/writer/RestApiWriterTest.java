package com.example.batch.writer;

import com.example.batch.model.CustomerOrderProductDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit test for RestApiWriter using mocked RestTemplate and RetryTemplate.
 */
class RestApiWriterTest {

    private RestTemplate restTemplate;
    private RetryTemplate retryTemplate;
    private RestApiWriter writer;

    @BeforeEach
    void setup() {
        restTemplate = mock(RestTemplate.class);
        retryTemplate = new RetryTemplate();
        writer = new RestApiWriter(restTemplate, retryTemplate, "http://mock-api");
    }

    @Test
    void testWrite_successfulPost() {
        // Arrange
        CustomerOrderProductDTO dto = new CustomerOrderProductDTO();
        dto.setOrderId(1L);
        dto.setCustomerName("Test");
        dto.setProductCode("P001");
        dto.setQuantity(2);
        dto.setPrice(99.99);

        when(restTemplate.postForEntity(any(), any(), eq(Void.class)))
                .thenReturn(null);

        // Act
        writer.write(Collections.singletonList(dto));

        // Assert
        verify(restTemplate, times(1)).postForEntity(any(), any(), eq(Void.class));
    }
}
