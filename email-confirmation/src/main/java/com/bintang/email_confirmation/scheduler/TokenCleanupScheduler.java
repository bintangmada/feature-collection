package com.bintang.email_confirmation.scheduler;

import com.bintang.email_confirmation.entity.User;
import com.bintang.email_confirmation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class TokenCleanupScheduler {

    @Autowired
    private UserRepository userRepository;

    @Scheduled(fixedRate = 60000)
    public void cleanupExpiredTokens(){
        List<User> expired = userRepository.findAll()
                .stream()
                .filter(u -> !u.isConfirmed() && u.getTokenExpiry().isBefore(LocalDateTime.now()))
                .toList();
        userRepository.deleteAll(expired);
    }
}
