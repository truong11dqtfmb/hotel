package com.dqt.hotel.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    @NotBlank
    private String fullName;

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    private String address;
    private String identity;
    private String phone;
    private List<String> roles;
}
