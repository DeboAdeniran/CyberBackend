package com.example.cyberbankend.Dto.Response;

public class VerifyOtpDto {
    private String message;

    public VerifyOtpDto() {
    }

    public VerifyOtpDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
