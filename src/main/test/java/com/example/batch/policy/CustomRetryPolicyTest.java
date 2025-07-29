package com.example.batch.policy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.retry.RetryContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit test for CustomRetryPolicy to validate retry conditions and limits.
 */
class CustomRetryPolicyTest {

    private CustomRetryPolicy customRetryPolicy;
    private RetryContext retryContext;

    @BeforeEach
    void setUp() {
        customRetryPolicy = new CustomRetryPolicy(3); // allow 3 retries
        retryContext = mock(RetryContext.class);
    }

    @Test
    void testCanRetry_WithRetryableException_UnderLimit() {
        when(retryContext.getRetryCount()).thenReturn(2);

        boolean result = customRetryPolicy.canRetry(retryContext);

        assertTrue(result, "Should retry when under max retry limit");
    }

    @Test
    void testCanRetry_ExceedsLimit() {
        when(retryContext.getRetryCount()).thenReturn(4);

        boolean result = customRetryPolicy.canRetry(retryContext);

        assertFalse(result, "Should not retry when retry count exceeds limit");
    }

    @Test
    void testCloseDoesNotThrow() {
        assertDoesNotThrow(() -> customRetryPolicy.close(retryContext));
    }

    @Test
    void testOpenDoesNotThrow() {
        assertDoesNotThrow(() -> customRetryPolicy.open(retryContext));
    }

    @Test
    void testRegisterThrowableDoesNotThrow() {
        assertDoesNotThrow(() -> customRetryPolicy.registerThrowable(retryContext, new RuntimeException("Test")));
    }
}
