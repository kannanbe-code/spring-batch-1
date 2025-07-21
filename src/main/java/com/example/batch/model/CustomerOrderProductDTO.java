package com.example.batch.model;

/**
 * This class represents a DTO that holds combined data
 * from multiple joined tables such as orders, customers, and products.
 * It's used to serialize/deserialize data between DB and REST API.
 */
public class CustomerOrderProductDTO {

    private Long orderId;
    private String customerName;
    private String productCode;
    private Integer quantity;
    private Double price;

    public CustomerOrderProductDTO() {
        // Default constructor
    }

    // Getters and Setters

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * For logging failed items or debugging purposes.
     */
    @Override
    public String toString() {
        return "CustomerOrderProductDTO{" +
                "orderId=" + orderId +
                ", customerName='" + customerName + '\'' +
                ", productCode='" + productCode + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}
