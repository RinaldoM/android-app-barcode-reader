package com.example.barcode;

public class OrderDto {
    private final String id;
    private final String orderNumber;
    private final String supplier;

    public OrderDto(String id, String orderNumber, String supplier) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.supplier = supplier;
    }

    public String getId() {
        return id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public String getSupplier() {
        return supplier;
    }
}
