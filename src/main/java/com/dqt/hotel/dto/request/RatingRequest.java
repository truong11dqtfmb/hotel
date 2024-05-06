package com.dqt.hotel.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatingRequest {
    private String description;

    @Size(min = 1, max = 5)
    private Integer rate;
}

