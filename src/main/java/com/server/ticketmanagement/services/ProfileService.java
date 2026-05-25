package com.server.ticketmanagement.services;

import com.server.ticketmanagement.domain.dtos.ProfileRequestDto;
import com.server.ticketmanagement.domain.dtos.ProfileResponseDto;

import java.util.UUID;

public interface ProfileService {

    ProfileResponseDto createProfile(ProfileRequestDto profileRequest);

    ProfileResponseDto getProfile(String email);

    void sendResetOTP(String email);

    void resetPassword(String email, String otp, String newPassword);

    void sendOTP(String email);

    void verifyOTP(String email, String otp);

    UUID getLoggedIdUserId(String email);

}

