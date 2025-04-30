package com.example.checkscam.config;

import com.example.checkscam.domain.User;
import com.example.checkscam.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        if (userRepository.count() == 0) {
            User defaultUser = new User();
            defaultUser.setName("Default Admin");
            defaultUser.setEmail("admin@example.com");
            defaultUser.setPassword(passwordEncoder.encode("123456"));

            userRepository.save(defaultUser);
            System.out.println("Tạo tài khoản mặc định thành công !");
        }
    }
}
