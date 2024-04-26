package com.dqt.hotel.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRequest {
    private String serviceName;
    private Integer hotelId;
    private String description;
}

