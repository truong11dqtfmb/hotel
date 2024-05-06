package com.dqt.hotel.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {
    @NotBlank
    private String content;
    @Size(min = 1, max = 2, message ="Type is 1: hotel || 2: review" )
    private Integer type;
//    1: hotel
//    2: review
    @NonNull
    private Integer commentId;
}

