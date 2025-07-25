package com.bintang.email_confirmation.service;

import com.bintang.email_confirmation.dto.RegistrationRequest;

public interface RegistrationService {
    void registerUser(RegistrationRequest request);
}
