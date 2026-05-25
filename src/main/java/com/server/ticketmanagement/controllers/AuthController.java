package com.server.ticketmanagement.controllers;
import com.server.ticketmanagement.domain.dtos.AuthRequestDto;
import com.server.ticketmanagement.domain.dtos.AuthResponseDto;
import com.server.ticketmanagement.domain.dtos.ResetPasswordRequestDto;
import com.server.ticketmanagement.services.ProfileService;
import com.server.ticketmanagement.services.impl.UserDetailServiceImpl;
import com.server.ticketmanagement.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;
    private final ProfileService profileService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDto request) {
        try {
            authenticate(request.getEmail(), request.getPassword());
            final UserDetails userDetails =  userDetailsService.loadUserByUsername(request.getEmail());
            final String jwtToken = jwtUtil.generateToken(userDetails);
            
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            ResponseCookie cookie = ResponseCookie.from("jwt", jwtToken)
                    .httpOnly(true)
                    .path("/")
                    .maxAge(10 * 60 * 60) // 10 hours
                    .sameSite("Strict")
                    .build();
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(new AuthResponseDto(request.getEmail(), jwtToken, roles));
        } catch (BadCredentialsException ex) {
            Logger.getLogger(AuthController.class.getName()).log(java.util.logging.Level.SEVERE, "Authentication failed for email: " + request.getEmail());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email or password");
        } catch (DisabledException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account is disabled");
        }
    }

    @GetMapping("/is-authenticated")
    public ResponseEntity<Boolean> isAuthenticated(@CurrentSecurityContext(expression = "authentication?.name") String email) {
        return ResponseEntity.ok(email != null);
    }


    // VERIFY PASSWORD ENDPOINTS
    @PostMapping("/send-otp")
    public void sendVerifyOTP(@CurrentSecurityContext(expression = "authentication?.name") String email) {
        try {
            profileService.sendOTP(email);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Couldn't send OTP. Try again later.");
        }
    }

    @PostMapping("/verify-otp")
    public void verifyEmail(@RequestBody Map<String, Object> request, @CurrentSecurityContext(expression = "authentication?.name") String email) {
        try {
            String otp = (String) request.get("otp");

            if (otp == null || otp.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP is required");
            }

            profileService.verifyOTP(email, otp);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Couldn't verify OTP. Try again later.");
        }
    }


    // RESET PASSWORD ENDPOINTS
    @PostMapping("/send-reset-otp")
    public ResponseEntity<?> sendResetOTP(@RequestParam String email) {
        try{
            profileService.sendResetOTP(email);
            return ResponseEntity.ok().body("OTP sent to email address.");
        } catch (Exception e) {
            Logger.getLogger(AuthController.class.getName()).log(java.util.logging.Level.SEVERE, "Reset OTP failed for: " + email + ", error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Couldn't send reset OTP");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequestDto resetPasswordRequestDto) {
        try{
            profileService.resetPassword(resetPasswordRequestDto.getEmail(), resetPasswordRequestDto.getOtp(), resetPasswordRequestDto.getNewPassword());
            return ResponseEntity.ok().body("Password has been restored successfully.");
        } catch (Exception e) {
            Logger.getLogger(AuthController.class.getName()).log(java.util.logging.Level.SEVERE, "Password reset failed for: " + resetPasswordRequestDto.getEmail() + ", error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Couldn't reset user password. Try again later.");
        }
    }

    private void authenticate(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }

}
