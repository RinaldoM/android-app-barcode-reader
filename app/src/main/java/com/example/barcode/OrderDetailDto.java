package com.example.barcode;

public class OrderDetailDto {
    private final String id;
    private final String article;
    private final String expectedAmount;

    public OrderDetailDto(String id, String article, String expectedAmount) {
        this.id = id;
        this.article = article;
        this.expectedAmount = expectedAmount;
    }

    public String getId() {
        return id;
    }

    public String getArticle() {
        return article;
    }

    public String getExpectedAmount() {
        return expectedAmount;
    }
}
