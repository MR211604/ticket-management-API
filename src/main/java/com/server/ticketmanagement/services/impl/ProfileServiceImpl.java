package com.server.ticketmanagement.services.impl;

import com.server.ticketmanagement.domain.dtos.ProfileRequestDto;
import com.server.ticketmanagement.domain.dtos.ProfileResponseDto;
import com.server.ticketmanagement.domain.entities.User;
import com.server.ticketmanagement.exceptions.UserAlreadyExistsException;
import com.server.ticketmanagement.exceptions.UserNotFoundException;
import com.server.ticketmanagement.repositories.UserRepository;
import com.server.ticketmanagement.services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailServiceImpl emailService;

    @Override
    public ProfileResponseDto createProfile(ProfileRequestDto profileRequest) {

        if(userRepository.existsByEmail(profileRequest.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        User newProfile = convertToUserEntity(profileRequest);
        newProfile = userRepository.save(newProfile);
        return convertToProfileEntity(newProfile);
    }

    @Override
    public ProfileResponseDto getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        return convertToProfileEntity(user);
    }

    @Override
    public void sendResetOTP(String email) {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

            String otp = generateOTP();
            long expirationTime = System.currentTimeMillis() + 15 * 60 * 1000; // OTP valid for 15 minutes

            user.setResetOTP(otp);
            user.setResetOTPExpiredAt(expirationTime);
            userRepository.save(user);

            try {
                emailService.sendResetOTPEmail(user.getEmail(), user.getName(), otp);
            } catch(Exception ex) {
                throw new RuntimeException("Failed to send reset OTP email: " + ex.getMessage());
            }

    }

    @Override
    public void resetPassword(String email, String otp, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        if (user.getResetOTP() == null || !user.getResetOTP().equals(otp) || System.currentTimeMillis() > user.getResetOTPExpiredAt()) {
            throw new IllegalArgumentException("Invalid or expired OTP");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetOTP(null);
        user.setResetOTPExpiredAt(0L);
        userRepository.save(user);
    }

    @Override
    public void sendOTP(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        if(user.getIsAccountVerified() != null && user.getIsAccountVerified()) {
            return;
        }

        String otp = generateOTP();

        long expirationTime = System.currentTimeMillis() + 24 * 60 * 60 * 1000; // OTP valid for 24 hours

        //Update user entity
        user.setVerifyOTP(otp);
        user.setVerifyOTPExpiredAt(expirationTime);
        userRepository.save(user);

        try {
            emailService.sendOTPEmail(user.getEmail(), user.getName(), otp);
        } catch(Exception ex) {
            throw new RuntimeException("Failed to send OTP email: " + ex.getMessage());
        }
    }

    @Override
    public void verifyOTP(String email, String otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        if (user.getVerifyOTP() == null || !user.getVerifyOTP().equals(otp) || System.currentTimeMillis() > user.getVerifyOTPExpiredAt()) {
            throw new IllegalArgumentException("Invalid or expired OTP");
        }

        user.setIsAccountVerified(true);
        user.setVerifyOTP(null);
        user.setVerifyOTPExpiredAt(0L);
        userRepository.save(user);
    }

    @Override
    public UUID getLoggedIdUserId(String email) {
         User existingUser = userRepository.findByEmail(email)
                 .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
         return existingUser.getId();
    }

    private String generateOTP() {
        return String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
    }

    private ProfileResponseDto convertToProfileEntity(User newProfile) {
        return ProfileResponseDto.builder()
                .userId(newProfile.getId())
                .email(newProfile.getEmail())
                .name(newProfile.getName())
                .userRole(newProfile.getUserRole())
                .isAccountVerified(newProfile.getIsAccountVerified())
                .build();
    }

    private User convertToUserEntity(ProfileRequestDto profileRequest) {
        return User.builder()
                .email(profileRequest.getEmail())
                .name(profileRequest.getName())
                .password(passwordEncoder.encode(profileRequest.getPassword()))
                .userRole(profileRequest.getUserRol())
                .isAccountVerified(false)
                .resetOTPExpiredAt(0L)
                .verifyOTP(null)
                .verifyOTPExpiredAt(0L)
                .resetOTP(null)
                .build();
    }
}
