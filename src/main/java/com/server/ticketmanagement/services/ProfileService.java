package com.server.ticketmanagement.services;

import com.server.ticketmanagement.domain.dtos.ProfileRequestDto;
import com.server.ticketmanagement.domain.dtos.ProfileResponseDto;

public interface ProfileService {

    ProfileResponseDto createProfile(ProfileRequestDto profileRequest);

    ProfileResponseDto getProfile(String email);

}
