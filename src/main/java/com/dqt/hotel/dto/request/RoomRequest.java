package com.dqt.hotel.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomRequest {
    private String roomName;
    private Integer hotelId;
    private String description;
    private Integer acreage;
    private Integer member;
    private Integer price;
}

