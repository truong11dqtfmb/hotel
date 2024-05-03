package com.dqt.hotel.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponse {
    private String roomName;
    private Integer hotelId;
    private String hotelName;
    private String description;
    private Integer acreage;
    private Integer member;
    private Integer price;
    private Integer status;
    private Date createdDate;
    private String createdBy;
    private Date modifiedDate;
    private String modifiedBy;
}

