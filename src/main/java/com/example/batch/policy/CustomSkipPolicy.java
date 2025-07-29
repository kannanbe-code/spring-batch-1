package com.example.batch.policy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.file.FlatFileParseException;

/**
 * CustomSkipPolicy determines whether an exception during item read/process/write should be skipped.
 * You can configure this instead of using `.skip(...)` and `.skipLimit(...)`.
 */
@Slf4j
public class CustomSkipPolicy implements SkipPolicy {

    /**
     * This method is called by Spring Batch when an exception is thrown during a chunk operation.
     *
     * @param throwable The exception that occurred
     * @param skipCount Number of skips that have already occurred so far
     * @return true if the exception should be skipped, false to fail the step
     */
    @Override
    public boolean shouldSkip(Throwable throwable, int skipCount) {
        log.warn("Skip check invoked for exception: {}, current skip count: {}", throwable.getClass().getSimpleName(), skipCount);

        // Skip parsing errors (e.g., CSV or flat file format issues)
        if (throwable instanceof FlatFileParseException) {
            log.warn("Skipping record due to flat file parse error. Total skips so far: {}", skipCount);
            return true;
        }

        // Skip a custom validation exception (user-defined)
        if (throwable instanceof com.example.batch.exception.InvalidCustomerDataException) {
            log.warn("Skipping record due to invalid customer data. Total skips so far: {}", skipCount);
            return true;
        }

        // All other exceptions will cause the step to fail
        return false;
    }
}
