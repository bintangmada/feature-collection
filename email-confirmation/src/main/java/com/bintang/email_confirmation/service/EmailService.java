package com.bintang.email_confirmation.service;

import com.bintang.email_confirmation.model.EmailMessage;

public interface EmailService {
    void sendConfirmationEmail(EmailMessage message);
}
