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
public class CustomerRecord {

    /**
     * Customer's unique ID.
     */
    private Long customerId;

    /**
     * Customer's full name.
     */
    private String customerName;

    /**
     * Email of the customer.
     */
    private String email;

    /**
     * Order ID associated with the customer (from joined orders table).
     */
    private Long orderId;

    /**
     * Order date for the above order.
     */
    private String orderDate;

    /**
     * Total amount of the order.
     */
    private Double totalAmount;

    /**
     * Name of the product ordered.
     */
    private String productName;

    /**
     * Country of the customer.
     */
    private String country;

    // Add more fields here as per your SQL join output


}
