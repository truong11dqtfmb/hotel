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
public class ServicesResponse {
    private String serviceName;
    private Integer hotelId;
    private String hotelName;
    private Date createdDate;
    private String createdBy;
    private Date modifiedDate;
    private String modifiedBy;
}

