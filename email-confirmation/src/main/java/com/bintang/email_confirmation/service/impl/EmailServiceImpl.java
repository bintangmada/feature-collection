package com.bintang.email_confirmation.service.impl;

import com.bintang.email_confirmation.model.EmailMessage;
import com.bintang.email_confirmation.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Override
    @Async
    public void sendConfirmationEmail(EmailMessage message) {
        jmsTemplate.convertAndSend("email.queue", message);
    }
}
