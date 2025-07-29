package com.example.batch.policy;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;

import static org.junit.jupiter.api.Assertions.*;

class CustomSkipPolicyTest {

    @Test
    void testSkipOnKnownExceptionWithinLimit() throws SkipLimitExceededException {
        CustomSkipPolicy policy = new CustomSkipPolicy();
        
        assertTrue(policy.shouldSkip(new IllegalArgumentException("Invalid"), 0));
        assertTrue(policy.shouldSkip(new IllegalArgumentException("Invalid again"), 1));
        assertTrue(policy.shouldSkip(new NullPointerException("Null pointer"), 2));
    }

    @Test
    void testDoNotSkipAfterMaxSkips() {
        CustomSkipPolicy policy = new CustomSkipPolicy();
        
        assertFalse(policy.shouldSkip(new IllegalArgumentException("Too many skips"), 5));
    }

    @Test
    void testDoNotSkipOnUnexpectedException() {
        CustomSkipPolicy policy = new CustomSkipPolicy();
        
        assertFalse(policy.shouldSkip(new RuntimeException("Unknown error"), 0));
    }
}
