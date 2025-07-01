package com.bintang.config;

import com.bintang.repository.UserRepository;
import com.bintang.service.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

import java.time.LocalDateTime;

@Configuration
public class SecurityConfig {

    private final CustomOAuth2UserService oAuth2UserService;
    private final UserRepository userRepository;

    public SecurityConfig(CustomOAuth2UserService oAuth2UserService, UserRepository userRepository) {
        this.oAuth2UserService = oAuth2UserService;
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           UserRepository userRepository) throws Exception{
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/","/index").permitAll()
                        .anyRequest().authenticated())
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(endpoint -> endpoint
                                .userService(oAuth2UserService)
                        )
                        .defaultSuccessUrl("/home", true)
                )
                .logout(logout -> logout.logoutSuccessUrl("/")
                        .addLogoutHandler((request, response, authentication) -> {
                            if(authentication != null && authentication.getPrincipal() instanceof OAuth2User user){
                                String email = user.getAttribute("email");

                                userRepository.findByEmail(email).ifPresent(u -> {
                                    u.setLastLogout(LocalDateTime.now());
                                    userRepository.save(u);
                                });
                            }
                        }));

        return http.build();
    }
}
