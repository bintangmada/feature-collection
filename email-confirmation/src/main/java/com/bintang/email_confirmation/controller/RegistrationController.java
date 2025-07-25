package com.bintang.email_confirmation.controller;

import com.bintang.email_confirmation.dto.RegistrationRequest;
import com.bintang.email_confirmation.entity.User;
import com.bintang.email_confirmation.repository.UserRepository;
import com.bintang.email_confirmation.service.RegistrationService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/register")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> register(@RequestBody RegistrationRequest request){
        registrationService.registerUser(request);
        return ResponseEntity.ok("Registrasi Berhasil. Silahkan cek email anda untuk mengonfirmasi");
    }

    @GetMapping("/confirm")
    public ResponseEntity<?> confirm(@RequestParam String token){
        Optional<User> user = userRepository.findByConfirmationToken(token);
        if(user.isPresent() && !user.get().isConfirmed() && user.get().getTokenExpiry().isAfter(LocalDateTime.now())){
            user.get().setConfirmed(true);
            userRepository.save(user.get());
            return ResponseEntity.ok("Email berhasil dikonfirmasi");
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token tidak valid atau sudah kadaluwarsa");
        }
    }
}
