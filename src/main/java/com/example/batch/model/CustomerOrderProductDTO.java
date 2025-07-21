package com.example.batch.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents a DTO that holds combined data
 * from multiple joined tables such as orders, customers, and products.
 * It's used to serialize/deserialize data between DB and REST API.
 *
 * Represents a customer record fetched from the database.
 * 
 * This model is used as the input and output of the batch step.
 * Lombok Annotations:
 * - @Data: Generates getters, setters, equals, hashCode, toString.
 * - @NoArgsConstructor: Default constructor.
 * - @AllArgsConstructor: Constructor with all fields.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerOrderProductDTO {

    private Long orderId;
    private String customerName;
    private String productCode;
    private Integer quantity;
    private Double price;


}
