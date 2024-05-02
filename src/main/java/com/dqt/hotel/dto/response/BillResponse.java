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
public class BillResponse {
    private Integer id;
    private Integer hotelId;
    private String hotelName;
    private Integer roomId;
    private String roomName;
    private Integer userId;
    private String fullName;
    private String email;
    private String phone;
    private String identity;
    private Integer bookingId;
    private Integer amount;
    private Date checkInDate;
    private Date checkOutDate;
    private Date paymentDate;
    private Date createdDate;
    private String createdBy;
    private Date modifiedDate;
    private String modifiedBy;
}

