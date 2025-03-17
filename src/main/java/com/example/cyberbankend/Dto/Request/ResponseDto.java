package com.example.cyberbankend.Dto.Request;

public class ResponseDto {
    private String email;
    private String phoneNumber;
    private String message;

    public ResponseDto() {
    }

    public ResponseDto(String email, String phoneNumber, String message) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    @Override
    public String toString() {
        return "ResponseDto{" +
                "email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
