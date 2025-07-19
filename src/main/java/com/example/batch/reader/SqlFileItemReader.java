package com.example.batch.reader;

import com.example.batch.model.CustomerOrderProductDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SqlFileItemReader implements ItemReader<CustomerOrderProductDTO> {

    private final JdbcTemplate jdbcTemplate;
    private Iterator<CustomerOrderProductDTO> resultsIterator;

    @PostConstruct
    public void loadQueryResults() throws IOException {
        String sql = Files.readString(new ClassPathResource("sql/complex_query.sql").getFile().toPath());

        List<CustomerOrderProductDTO> results = jdbcTemplate.query(sql, (rs, rowNum) -> {
            CustomerOrderProductDTO dto = new CustomerOrderProductDTO();
            dto.setCustomerName(rs.getString("customerName"));
            dto.setProductName(rs.getString("productName"));
            dto.setQuantity(rs.getInt("quantity"));
            dto.setOrderDate(rs.getString("orderDate"));
            return dto;
        });

        resultsIterator = results.iterator();
    }

    @Override
    public CustomerOrderProductDTO read() {
        if (resultsIterator != null && resultsIterator.hasNext()) {
            return resultsIterator.next();
        }
        return null;
    }
}
