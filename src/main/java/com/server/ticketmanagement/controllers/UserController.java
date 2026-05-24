package com.server.ticketmanagement.controllers;


import com.server.ticketmanagement.domain.dtos.ProfileRequestDto;
import com.server.ticketmanagement.domain.dtos.ProfileResponseDto;
import com.server.ticketmanagement.services.ProfileService;
import com.server.ticketmanagement.services.impl.EmailServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/auth")
public class UserController {

    private final ProfileService profileService;
    private final EmailServiceImpl emailService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ProfileResponseDto register(@Valid @RequestBody ProfileRequestDto request) {
        ProfileResponseDto response =  profileService.createProfile(request);
        emailService.sendWelcomeEmail(response.getEmail(), response.getName());
        return response;
    }

    @GetMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    public ProfileResponseDto getProfile(@CurrentSecurityContext(expression = "authentication?.name") String email) {
        return profileService.getProfile(email);
   }



}
