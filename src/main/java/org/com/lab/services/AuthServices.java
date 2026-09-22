package org.com.lab.services;

import org.com.lab.dto.request.LoginRequest;
import org.com.lab.dto.request.RegisterRequest;
import org.com.lab.dto.response.LoginResponse;
import org.com.lab.entity.AuthUser;
import org.com.lab.entity.enums.UserRole;
import org.com.lab.entity.enums.UserStatus;
import org.com.lab.error.AuthErrorCode;
import org.com.lab.repository.AuthUserJpaRepository;
import org.example.javaframework.infra.security.JwtProvider;
import org.example.javaframework.web.common.EnumConverter;
import org.example.javaframework.web.exception.BusinessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthServices {

    private final AuthUserJpaRepository userJpaRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;

    public AuthServices(AuthUserJpaRepository userJpaRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.userJpaRepository = userJpaRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    public void register(RegisterRequest registerRequest) {
        String username = registerRequest.username();
        String password = registerRequest.password();
        String role = registerRequest.role();
        if(userJpaRepository.existsByUsername(username)) {
            throw new BusinessException(AuthErrorCode.USER_ALREADY_EXISTS, username);
        }
        AuthUser user = new AuthUser();
        user.setId(UUID.randomUUID().toString().replace("-", "").substring(0, 25));
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setRole(EnumConverter.fromString(UserRole.class, role));
        user.setStatus(UserStatus.ACTIVE);
        userJpaRepository.save(user);
    }

    public LoginResponse login(LoginRequest loginRequest) {
        String username = loginRequest.username();
        String password = loginRequest.password();
        AuthUser user = userJpaRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(AuthErrorCode.USER_NOT_FOUND, username));
        if(!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new BusinessException(AuthErrorCode.INVALID_CREDENTIALS);
        }
        String token = jwtProvider.generateToken(user.getId(),user.getUsername(), user.getRole().name(), user.getStatus().toString());
        return new LoginResponse(token);
    }

}
