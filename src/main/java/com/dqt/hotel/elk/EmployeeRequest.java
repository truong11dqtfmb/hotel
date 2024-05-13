package com.dqt.hotel.elk;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequest {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String language;
}
