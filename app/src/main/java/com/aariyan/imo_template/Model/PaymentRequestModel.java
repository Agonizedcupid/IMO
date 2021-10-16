package com.aariyan.imo_template.Model;

public class PaymentRequestModel {
    private String id;
    private String point;
    private String userId;
    private String date;
    private String time;
    private String phoneNumber;
    private String message;
    private String status;

    public PaymentRequestModel() {}

    public PaymentRequestModel(String id, String point, String userId, String date, String time, String phoneNumber, String message, String status) {
        this.id = id;
        this.point = point;
        this.userId = userId;
        this.date = date;
        this.time = time;
        this.phoneNumber = phoneNumber;
        this.message = message;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
