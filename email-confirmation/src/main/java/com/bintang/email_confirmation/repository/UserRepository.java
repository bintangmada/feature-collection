package com.bintang.email_confirmation.repository;

import com.bintang.email_confirmation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
