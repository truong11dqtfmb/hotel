package com.dqt.hotel.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvatarRequest {
    private MultipartFile file;
    private Integer type;
    private Integer id;
}

