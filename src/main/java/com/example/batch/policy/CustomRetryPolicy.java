package com.example.batch.policy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryContextSupport;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom retry policy to define which exceptions are retryable and how many attempts are allowed.
 */
@Slf4j
public class CustomRetryPolicy implements RetryPolicy {

    private final SimpleRetryPolicy delegatePolicy;

    public CustomRetryPolicy() {
        Map<Class<? extends Throwable>, Boolean> retryableExceptions = new HashMap<>();
        retryableExceptions.put(SocketTimeoutException.class, true);
        retryableExceptions.put(org.springframework.web.client.HttpServerErrorException.class, true);
        retryableExceptions.put(org.springframework.web.client.ResourceAccessException.class, true);

        this.delegatePolicy = new SimpleRetryPolicy(3, retryableExceptions);
    }

    @Override
    public boolean canRetry(RetryContext context) {
        return delegatePolicy.canRetry(context);
    }

    @Override
    public RetryContext open(RetryContext parent) {
        return new RetryContextSupport(parent);
    }

    @Override
    public void close(RetryContext context) {
        // No-op
    }

    @Override
    public void registerThrowable(RetryContext context, Throwable throwable) {
        log.warn("Retry attempt due to exception: {}", throwable.getClass().getSimpleName());
        delegatePolicy.registerThrowable(context, throwable);
    }
}
