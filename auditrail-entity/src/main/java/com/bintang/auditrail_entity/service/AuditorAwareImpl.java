package com.bintang.auditrail_entity.service;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        // Ganti dengan user yang login kalau sudah pakai Spring Security
        return Optional.of("bintang");
    }
}
