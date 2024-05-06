package com.dqt.hotel.dto.request;

import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {
    @NonNull
    private Integer hotelId;

    private Integer roomId;
    private String description;

    @NonNull
    private Date checkInDate;
    @NonNull
    private Date checkOutDate;

}

