package com.dqt.hotel.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomRequest {
    @NotBlank
    private String roomName;

    @NonNull
    private Integer hotelId;
    private String description;

    @NonNull
    private Integer acreage;

    @NonNull
    private Integer member;

    @NonNull
    private Integer price;
}

