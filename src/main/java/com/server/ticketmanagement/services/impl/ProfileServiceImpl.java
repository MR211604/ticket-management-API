package com.server.ticketmanagement.services.impl;

import com.server.ticketmanagement.domain.dtos.ProfileRequestDto;
import com.server.ticketmanagement.domain.dtos.ProfileResponseDto;
import com.server.ticketmanagement.domain.entities.User;
import com.server.ticketmanagement.exceptions.UserAlreadyExistsException;
import com.server.ticketmanagement.repositories.UserRepository;
import com.server.ticketmanagement.services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;

    @Override
    public ProfileResponseDto createProfile(ProfileRequestDto profileRequest) {

        if(userRepository.existsByEmail(profileRequest.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        User newProfile = convertToUserEntity(profileRequest);
        newProfile = userRepository.save(newProfile);
        return convertToProfileEntity(newProfile);
    }

    private ProfileResponseDto convertToProfileEntity(User newProfile) {
        return ProfileResponseDto.builder()
                .userId(newProfile.getId())
                .email(newProfile.getEmail())
                .name(newProfile.getName())
                .isAccountVerified(newProfile.getIsAccountVerified())
                .build();
    }

    private User convertToUserEntity(ProfileRequestDto profileRequest) {
        return User.builder()
                .email(profileRequest.getEmail())
                .name(profileRequest.getName())
                .password(profileRequest.getPassword())
                .isAccountVerified(false)
                .resetOTPExpiredAt(0L)
                .verifyOTP(null)
                .verifyOTPExpiredAt(0L)
                .resetOTP(null)
                .build();
    }
}
