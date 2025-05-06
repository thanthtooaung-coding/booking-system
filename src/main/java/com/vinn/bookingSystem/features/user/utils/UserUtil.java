package com.vinn.bookingSystem.features.user.utils;

import com.vinn.bookingSystem.config.exceptions.UnauthorizedException;
import com.vinn.bookingSystem.features.user.dto.response.UserDto;
import com.vinn.bookingSystem.features.user.entity.User;
import com.vinn.bookingSystem.features.user.repository.UserRepository;
import com.vinn.bookingSystem.security.service.JwtService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserUtil {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserUtil(final JwtService jwtService, final UserRepository userRepository, final ModelMapper modelMapper) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public UserDto getCurrentUserDto(final String authHeader) {
        String email = extractEmailFromToken(authHeader);
        User user = findUserByEmail(email);
        return modelMapper.map(user, UserDto.class);
    }

    public String extractEmailFromToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Invalid Authorization header received");
            throw new UnauthorizedException("Unauthorized: Missing or invalid token");
        }

        String token = authHeader.substring(7);
        Claims claims = jwtService.validateToken(token);
        return claims.getSubject();
    }


    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("User not found for email: {}", email);
                    return new UnauthorizedException("User not found");
                });
    }
}
