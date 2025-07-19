package com.example.batch.model;

import lombok.Data;

@Data
public class CustomerOrderProductDTO {
    private String customerName;
    private String productName;
    private int quantity;
    private String orderDate;
}
