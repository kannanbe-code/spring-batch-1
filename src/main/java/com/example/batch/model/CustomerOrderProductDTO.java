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

     /**
     * Customer's unique identifier.
     */
    private Long customerId;

    /**
     * Full name of the customer.
     */
    private String customerName;

    /**
     * Email address of the customer.
     */
    private String email;

    /**
     * Country where the customer resides.
     */
    private String country;

    /**
     * Order ID associated with the customer.
     */
    private Long orderId;

    /**
     * Date the order was placed.
     */
    private String orderDate;

    /**
     * Total amount of the order.
     */
    private Double totalAmount;

    /**
     * Product name from the joined product table.
     */
    private String productName;

    /**
     * Quantity of the product ordered.
     */
    private Integer quantity;

    /**
     * Unit price of the product.
     */
    private Double unitPrice;

    // Add more fields here if your SQL join result includes additional columns.

}
