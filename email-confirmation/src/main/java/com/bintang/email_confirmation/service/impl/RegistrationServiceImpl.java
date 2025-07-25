package com.bintang.email_confirmation.service.impl;

import com.bintang.email_confirmation.dto.RegistrationRequest;
import com.bintang.email_confirmation.entity.User;
import com.bintang.email_confirmation.model.EmailMessage;
import com.bintang.email_confirmation.repository.UserRepository;
import com.bintang.email_confirmation.service.EmailService;
import com.bintang.email_confirmation.service.RegistrationService;
import com.bintang.email_confirmation.util.TokenGenerator;
import jakarta.websocket.OnClose;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Value("${app.confirmation.url}")
    private String confirmationBaseUrl;

    @Override
    public void registerUser(RegistrationRequest request){
        String token = TokenGenerator.generateToken();
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(15);

        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setConfirmationToken(token);
        user.setTokenExpiry(expiry);
        user.setConfirmed(false);
        userRepository.save(user);

        String url = confirmationBaseUrl+"?token="+token;

        Map<String, Object> variables = new HashMap<>();
        variables.put("name", user.getName());
        variables.put("confirmationUrl", url);

        EmailMessage message = new EmailMessage(
                user.getEmail(),
                "Konfirmasi Email Anda",
                "confirmation-email",
                variables
        );

        emailService.sendConfirmationEmail(message);
    }
}
