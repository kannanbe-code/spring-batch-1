package com.example.batch.policy;

import org.junit.jupiter.api.Test;
import org.springframework.batch.retry.RetryContext;
import org.springframework.batch.retry.policy.SimpleRetryPolicy;

import static org.junit.jupiter.api.Assertions.*;

class CustomRetryPolicyTest {

    @Test
    void testCanRetryWithinLimit() {
        SimpleRetryPolicy policy = new SimpleRetryPolicy(3);
        RetryContext context = policy.open(null);

        assertTrue(policy.canRetry(context));
        policy.registerThrowable(context, new RuntimeException("First failure"));

        assertTrue(policy.canRetry(context));
        policy.registerThrowable(context, new RuntimeException("Second failure"));

        assertTrue(policy.canRetry(context));
        policy.registerThrowable(context, new RuntimeException("Third failure"));

        // Exceeded max attempts
        assertFalse(policy.canRetry(context));
    }
}
