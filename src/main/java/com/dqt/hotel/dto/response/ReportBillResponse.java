package com.dqt.hotel.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportBillResponse {
    LocalDate date;
    Integer month;
    Integer year;
    String total;

}

