package com.example.batch.policy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for CustomSkipPolicy to verify skip conditions and limits.
 */
class CustomSkipPolicyTest {

    private CustomSkipPolicy customSkipPolicy;

    @BeforeEach
    void setUp() {
        customSkipPolicy = new CustomSkipPolicy(3); // allow up to 3 skips
    }

    @Test
    void testShouldSkip_RetryableException_UnderSkipLimit() {
        Exception exception = new IllegalArgumentException("Simulated recoverable failure");

        boolean result = customSkipPolicy.shouldSkip(exception, 2);

        assertTrue(result, "Should skip when under skip limit and exception is retryable");
    }

    @Test
    void testShouldSkip_ExceedsSkipLimit() {
        Exception exception = new IllegalArgumentException("Simulated recoverable failure");

        boolean result = customSkipPolicy.shouldSkip(exception, 4);

        assertFalse(result, "Should not skip when skip limit exceeded");
    }

    @Test
    void testShouldSkip_NonRetryableException() {
        Exception exception = new NullPointerException("Critical failure");

        boolean result = customSkipPolicy.shouldSkip(exception, 0);

        assertFalse(result, "Should not skip non-retryable exceptions like NullPointerException");
    }
}
