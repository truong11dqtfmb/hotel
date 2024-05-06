package com.dqt.hotel.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRequest {
    @NotBlank
    private String serviceName;

    @NonNull
    private Integer hotelId;

    private String description;
}

