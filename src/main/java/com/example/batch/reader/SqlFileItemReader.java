package com.example.batch.reader;

import com.example.batch.model.CustomerOrderProductDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * ItemReader implementation that:
 * - Loads a complex SQL SELECT query from a .sql file
 * - Executes the query using JdbcCursorItemReader
 * - Maps each result row to a CustomerOrderProductDTO
 *
 * This is used to read all data to be processed by the batch step.
 */
@Slf4j
@Component
public class SqlFileItemReader extends JdbcCursorItemReader<CustomerOrderProductDTO> {

    private static final String SQL_FILE_PATH = "sql/customer_order_query.sql";

    /**
     * Constructor to set up the reader with the SQL query and row mapper.
     *
     * @param dataSource injected datasource (Oracle)
     */
    public SqlFileItemReader(DataSource dataSource) {
        super.setDataSource(dataSource);
        super.setRowMapper(new BeanPropertyRowMapper<>(CustomerOrderProductDTO.class));
        super.setVerifyCursorPosition(false); // Improves Oracle cursor handling
    }

    /**
     * Initializes the reader by loading the SQL from the .sql file.
     * This method is executed after bean construction.
     */
    @PostConstruct
    public void init() {
        try {
            // Load SQL file from classpath (src/main/resources/sql)
            ClassPathResource resource = new ClassPathResource(SQL_FILE_PATH);
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

                // Join all lines into a single SQL string
                String sql = reader.lines().collect(Collectors.joining("\n"));
                log.info("Loaded SQL query from file:\n{}", sql);
                this.setSql(sql); // Set the SQL for the reader to execute

            }
        } catch (Exception e) {
            log.error("Failed to load SQL file: {}", SQL_FILE_PATH, e);
            throw new RuntimeException("Error reading SQL file for SqlFileItemReader", e);
        }
    }
}
