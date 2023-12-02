package com.example.tomatoAccounting.service;


import com.example.tomatoAccounting.config.SecurityConfig;
import com.example.tomatoAccounting.entity.UserEntity;
import com.example.tomatoAccounting.entity.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    SecurityConfig securityConfig;


//    @EventListener(ApplicationReadyEvent.class)
//    public void saveAdmin() {
//        UserEntity admin = new UserEntity("admin", securityConfig.getBcryptPasswordEncoder().encode("admin"), "ROLE_ADMIN");
//        userRepository.save(admin);
//    }

    public ResponseEntity<?> createUser(String email, String password) {

        UserEntity userEntity = new UserEntity(email, securityConfig.getBcryptPasswordEncoder().encode(password), "ROLE_USER");
        return ResponseEntity.ok(userRepository.save(userEntity));

    }
}
