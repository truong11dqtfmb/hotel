package com.dqt.hotel.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    @NotBlank
    private String fullName;

    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 6, max = 24)
    private String password;

    private String address;
    private String identity;
    private String phone;
}
