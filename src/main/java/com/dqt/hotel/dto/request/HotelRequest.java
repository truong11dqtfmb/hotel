package com.dqt.hotel.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelRequest {
    @NotBlank
    private String hotelName;

    @NotBlank
    private String address;
    private String description;
}

