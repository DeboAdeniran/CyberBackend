package com.example.cyberbankend.Dto.Response;

public class LoginResponse {
    private String email;
    private String phoneNumber;
    private String password;

    public LoginResponse() {
    }

    public LoginResponse(String email, String phoneNumber, String password) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
