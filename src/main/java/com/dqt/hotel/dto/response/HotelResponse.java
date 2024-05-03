package com.dqt.hotel.dto.response;

import com.dqt.hotel.entity.Room;
import com.dqt.hotel.entity.Services;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelResponse {
    private Integer id;
    private String hotelName;
    private String address;
    private String description;
    private Date createdDate;
    private String createdBy;
    private Date modifiedDate;
    private String modifiedBy;
    private List<String> avatar;
    private List<Room> rooms;
    private List<Services> services;

}

