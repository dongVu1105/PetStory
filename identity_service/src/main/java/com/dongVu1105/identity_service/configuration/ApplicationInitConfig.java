package com.dongVu1105.identity_service.configuration;


import com.dongVu1105.identity_service.constant.PredefinedRole;
import com.dongVu1105.identity_service.entity.Role;
import com.dongVu1105.identity_service.entity.User;
import com.dongVu1105.identity_service.exception.AppException;
import com.dongVu1105.identity_service.exception.ErrorCode;
import com.dongVu1105.identity_service.repository.RoleRepository;
import com.dongVu1105.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Configuration
@Slf4j
public class ApplicationInitConfig {
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        log.info("Initializing application.....");
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                Set<Role> roles = new HashSet<>();
                roles.add(roleRepository.findById(PredefinedRole.ADMIN_ROLE).orElseThrow(
                        () -> new AppException(ErrorCode.ROLE_NOT_EXISTED)));
                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .roles(roles)
                        .build();
                userRepository.save(user);
                log.warn("admin user has been created with default password: admin, please change it");
            }
        };
    }
}
