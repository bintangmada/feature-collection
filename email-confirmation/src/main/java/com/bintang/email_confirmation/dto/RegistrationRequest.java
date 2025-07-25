package com.bintang.email_confirmation.dto;

public class RegistrationRequest {
    private String name;
    private String email;

    public RegistrationRequest(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public RegistrationRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
