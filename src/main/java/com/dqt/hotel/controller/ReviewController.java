package com.dqt.hotel.controller;

import com.dqt.hotel.constant.Authority;
import com.dqt.hotel.dto.request.ReviewRequest;
import com.dqt.hotel.dto.response.ResponseMessage;
import com.dqt.hotel.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/review")

@Slf4j
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<?> getListReview(@RequestParam(required = false, defaultValue = "1") Integer page,
                                           @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                                           @RequestParam(required = false) Integer hotelId,
                                           @RequestParam(required = false) String key) {
        try {
            log.info("Start getListReview with request: {}, {}, {}", hotelId, key);
            Map<String, Object> result = reviewService.getListReview(page, pageSize, hotelId, key);
            log.info("End getListReview: {}", result);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error getListReview: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> reviewHotel(@PathVariable Integer id, @RequestBody ReviewRequest request) {
        try {
            log.info("Start reviewHotel with request: {}", id);
            ResponseMessage responseMessage = reviewService.reviewHotel(request, id);

            log.info("End reviewHotel: {}", responseMessage);
            if (responseMessage.isStatus()) return ResponseEntity.ok(responseMessage);
            return ResponseEntity.badRequest().body(responseMessage);
        } catch (Exception e) {
            log.error("Error reviewHotel: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @PreAuthorize(Authority.ADMIN)
    public ResponseEntity<?> deleteReview(@PathVariable Integer id) {
        try {
            log.info("Start deleteReview with request: {}", id);
            ResponseMessage responseMessage = reviewService.deleteReview(id);

            log.info("End deleteReview: {}", responseMessage);
            if (responseMessage.isStatus()) return ResponseEntity.ok(responseMessage);
            return ResponseEntity.badRequest().body(responseMessage);
        } catch (Exception e) {
            log.error("Error deleteReview: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}

