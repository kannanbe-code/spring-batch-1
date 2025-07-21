package com.example.batch.reader;

import com.example.batch.model.CustomerOrderProductDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.support.IteratorItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A custom Spring Batch reader that loads a complex SQL query from a file,
 * executes it using JdbcTemplate (Oracle-compatible), and returns results
 * as an ItemReader via an internal IteratorItemReader.
 */
@Component
public class SqlFileItemReader extends IteratorItemReader<CustomerOrderProductDTO> {

    private static final Logger logger = LoggerFactory.getLogger(SqlFileItemReader.class);

    private final JdbcTemplate jdbcTemplate;

    // Injected from application.yml - points to the SQL file in classpath or filesystem
    @Value("classpath:sql/query.sql")
    private Resource sqlFile;

    public SqlFileItemReader(JdbcTemplate jdbcTemplate) {
        super(null); // Iterator will be set in @PostConstruct
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Initializes the reader by reading the SQL from file and executing it
     * to fetch all rows into memory. Not ideal for huge data sets.
     */
    @PostConstruct
    public void init() {
        try {
            // Read SQL query from the file
            String sql = new BufferedReader(new InputStreamReader(sqlFile.getInputStream(), StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining(" "));

            logger.info("Executing SQL: {}", sql);

            // Execute SQL and map results to DTOs
            List<CustomerOrderProductDTO> results = jdbcTemplate.query(sql, rowMapper());

            logger.info("Loaded {} records from DB", results.size());

            // Provide iterator to parent class
            this.setIterator(results.iterator());

        } catch (Exception e) {
            logger.error("Failed to read or execute SQL file", e);
            throw new RuntimeException("Error initializing SqlFileItemReader", e);
        }
    }

    /**
     * Maps rows from ResultSet to CustomerOrderProductDTO.
     * Update this mapping based on your column names and structure.
     */
    private RowMapper<CustomerOrderProductDTO> rowMapper() {
        return (rs, rowNum) -> {
            CustomerOrderProductDTO dto = new CustomerOrderProductDTO();
            dto.setOrderId(rs.getLong("order_id"));
            dto.setCustomerName(rs.getString("customer_name"));
            dto.setProductCode(rs.getString("product_code"));
            dto.setQuantity(rs.getInt("quantity"));
            dto.setPrice(rs.getDouble("price"));
            return dto;
        };
    }

    @Override
    public CustomerOrderProductDTO read() {
        if (resultsIterator != null && resultsIterator.hasNext()) {
            return resultsIterator.next();
        }
        return null;
    }
}
