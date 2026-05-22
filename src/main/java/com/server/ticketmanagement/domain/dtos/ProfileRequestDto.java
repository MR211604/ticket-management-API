package com.server.ticketmanagement.domain.dtos;


import com.server.ticketmanagement.domain.entities.UserRoleEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileRequestDto {

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "User role is required")
    private UserRoleEnum userRol;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}
