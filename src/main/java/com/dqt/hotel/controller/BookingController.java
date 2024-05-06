package com.dqt.hotel.controller;

import com.dqt.hotel.constant.Authority;
import com.dqt.hotel.dto.request.BookingRequest;
import com.dqt.hotel.dto.response.ResponseMessage;
import com.dqt.hotel.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/booking")

@Slf4j
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    public ResponseEntity<?> getListBooking(@RequestParam(required = false, defaultValue = "1") Integer page,
                                            @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                                            @RequestParam(required = false) Integer hotelId,
                                            @RequestParam(required = false) Integer roomId,
                                            @RequestParam(required = false) Integer userId,
                                            @RequestParam(required = false) Date start,
                                            @RequestParam(required = false) Date end) {
        try {
            log.info("Start getListBooking with request: {}, {}, {}", page, pageSize, hotelId);
            Map<String, Object> result = bookingService.listBooking(page, pageSize, hotelId, roomId, userId, start, end);
            log.info("End getListBooking: {}", result);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error getListBooking: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    @PreAuthorize(Authority.ADMIN)
    public ResponseEntity<?> addBooking(@RequestBody BookingRequest request) {
        try {
            log.info("Start addBooking with request: {}", request);
            ResponseMessage responseMessage = bookingService.addBooking(request);
            log.info("End addBooking: {}", responseMessage);
            if (responseMessage.isStatus()) return ResponseEntity.ok(responseMessage);
            return ResponseEntity.badRequest().body(responseMessage);
        } catch (Exception e) {
            log.error("Error addBooking: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @PreAuthorize(Authority.ADMIN)
    public ResponseEntity<?> editBooking(@RequestBody BookingRequest request, @PathVariable Integer id) {
        try {
            log.info("Start editBooking with request: {}, {}", request, id);
            ResponseMessage responseMessage = bookingService.editBooking(request, id);
            log.info("End editBooking: {}", responseMessage);
            if (responseMessage.isStatus()) return ResponseEntity.ok(responseMessage);
            return ResponseEntity.badRequest().body(responseMessage);
        } catch (Exception e) {
            log.error("Error editBooking: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getBooking(@PathVariable Integer id) {
        try {
            log.info("Start getBooking with request: {}", id);
            ResponseMessage responseMessage = bookingService.getBooking(id);
            log.info("End getBooking: {}", responseMessage);
            if (responseMessage.isStatus()) return ResponseEntity.ok(responseMessage);
            return ResponseEntity.badRequest().body(responseMessage);
        } catch (Exception e) {
            log.error("Error getBooking: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("user/{userId}")
    public ResponseEntity<?> getUserBooking(@PathVariable Integer userId) {
        try {
            log.info("Start getUserBooking with request: {}", userId);
            ResponseMessage responseMessage = bookingService.getBookingByUser(userId);
            log.info("End getUserBooking: {}", responseMessage);
            if (responseMessage.isStatus()) return ResponseEntity.ok(responseMessage);
            return ResponseEntity.badRequest().body(responseMessage);
        } catch (Exception e) {
            log.error("Error getUserBooking: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}

