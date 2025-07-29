package com.example.batch.policy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.file.FlatFileParseException;

/**
 * Custom skip policy that defines which exceptions should be skipped during processing.
 */
@Slf4j
public class CustomSkipPolicy implements SkipPolicy {

    @Override
    public boolean shouldSkip(Throwable t, int skipCount) {
        if (t instanceof FlatFileParseException) {
            log.warn("Skipping record due to flat file parse error at count {}", skipCount);
            return true;
        }

        if (t instanceof com.example.batch.exception.InvalidCustomerDataException) {
            log.warn("Skipping record due to invalid customer data at count {}", skipCount);
            return true;
        }

        return false;
    }
}
