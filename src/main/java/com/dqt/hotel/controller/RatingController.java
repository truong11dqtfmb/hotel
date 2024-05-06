package com.dqt.hotel.controller;

import com.dqt.hotel.constant.Authority;
import com.dqt.hotel.dto.request.RatingRequest;
import com.dqt.hotel.dto.response.ResponseMessage;
import com.dqt.hotel.service.RatingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/rating")

@Slf4j
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @GetMapping
    public ResponseEntity<?> getListRating(@RequestParam(required = false, defaultValue = "1") Integer page,
                                           @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                                           @RequestParam(required = false) Integer hotelId,
                                           @RequestParam(required = false) Integer rate) {
        try {
            log.info("Start getListRating with request: {}, {}, {}", hotelId, rate);
            Map<String, Object> result = ratingService.getListRating(page, pageSize, hotelId, rate);
            log.info("End getListRating: {}", result);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error getListRating: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("calculate-rating/{id}")
    public ResponseEntity<?> calculateRatingHotel(@PathVariable Integer id) {
        try {
            log.info("Start calculateRatingHotel with request: {}", id);
            ResponseMessage responseMessage = ratingService.calculateRatingHotel(id);

            log.info("End calculateRatingHotel: {}", responseMessage);
            if (responseMessage.isStatus()) return ResponseEntity.ok(responseMessage);
            return ResponseEntity.badRequest().body(responseMessage);
        } catch (Exception e) {
            log.error("Error calculateRatingHotel: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> ratingHotel(@PathVariable Integer id, @RequestBody RatingRequest request) {
        try {
            log.info("Start ratingHotel with request: {}", id);
            ResponseMessage responseMessage = ratingService.ratingHotel(request, id);

            log.info("End ratingHotel: {}", responseMessage);
            if (responseMessage.isStatus()) return ResponseEntity.ok(responseMessage);
            return ResponseEntity.badRequest().body(responseMessage);
        } catch (Exception e) {
            log.error("Error ratingHotel: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @PreAuthorize(Authority.ADMIN)
    public ResponseEntity<?> deleteRating(@PathVariable Integer id) {
        try {
            log.info("Start deleteRating with request: {}", id);
            ResponseMessage responseMessage = ratingService.deleteRating(id);

            log.info("End deleteRating: {}", responseMessage);
            if (responseMessage.isStatus()) return ResponseEntity.ok(responseMessage);
            return ResponseEntity.badRequest().body(responseMessage);
        } catch (Exception e) {
            log.error("Error deleteRating: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("check/{id}")
    public ResponseEntity<?> checkRating(@PathVariable Integer id) {
        try {
            log.info("Start checkRating with request: {}", id);
            ResponseMessage responseMessage = ratingService.checkRating(id);

            log.info("End checkRating: {}", responseMessage);
            if (responseMessage.isStatus()) return ResponseEntity.ok(responseMessage);
            return ResponseEntity.badRequest().body(responseMessage);
        } catch (Exception e) {
            log.error("Error checkRating: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}

