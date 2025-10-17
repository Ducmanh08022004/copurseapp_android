package com.example.courseapp.model.request;

public class CreatePaymentRequest {
    private int orderId;
    private String method;

    public CreatePaymentRequest(int orderId, String method) {
        this.orderId = orderId;
        this.method = method;
    }
}
