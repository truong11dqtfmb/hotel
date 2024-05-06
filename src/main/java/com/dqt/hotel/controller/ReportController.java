package com.dqt.hotel.controller;

import com.dqt.hotel.dto.response.ResponseMessage;
import com.dqt.hotel.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/report")

@Slf4j
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;


    @GetMapping("booking/{type}")
    public ResponseEntity<?> reportBooking(@PathVariable Integer type) {
        try {
            log.info("Start reportBooking with request: {}", type);
            ResponseMessage responseMessage = reportService.reportBooking(type);

            log.info("End reportBooking: {}", responseMessage);
            if (responseMessage.isStatus()) return ResponseEntity.ok(responseMessage);
            return ResponseEntity.badRequest().body(responseMessage);
        } catch (Exception e) {
            log.error("Error reportBooking: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("bill/{type}")
    public ResponseEntity<?> reportBill(@PathVariable Integer type) {
        try {
            log.info("Start reportBill with request: {}", type);
            ResponseMessage responseMessage = reportService.reportBill(type);

            log.info("End reportBill: {}", responseMessage);
            if (responseMessage.isStatus()) return ResponseEntity.ok(responseMessage);
            return ResponseEntity.badRequest().body(responseMessage);
        } catch (Exception e) {
            log.error("Error reportBill: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}

