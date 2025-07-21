package com.example.batch.reader;

import com.example.batch.model.CustomerOrderProductDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

class SqlFileItemReaderTest {

    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        jdbcTemplate = mock(JdbcTemplate.class);
    }

    @Test
    void testInit_executesSQL() {
        // Arrange
        SqlFileItemReader reader = new SqlFileItemReader(jdbcTemplate);
        String mockSql = "SELECT * FROM DUAL"; // simple test query
        reader.sqlFile = new ByteArrayResource(mockSql.getBytes());

        when(jdbcTemplate.query(anyString(), any())).thenReturn(Collections.emptyList());

        // Act & Assert
        assertDoesNotThrow(reader::init);

        verify(jdbcTemplate, times(1)).query(anyString(), any());
    }
}
