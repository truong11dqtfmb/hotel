package com.dqt.hotel.controller;

import com.dqt.hotel.constant.Authority;
import com.dqt.hotel.dto.response.ResponseMessage;
import com.dqt.hotel.service.BillService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/bill")

@Slf4j
@RequiredArgsConstructor
public class BillController {

    private final BillService billService;

    @GetMapping
    @PreAuthorize(Authority.ADMIN_OR_USER)
    public ResponseEntity<?> getListBill(@RequestParam(required = false, defaultValue = "1") Integer page, @RequestParam(required = false, defaultValue = "20") Integer pageSize, @RequestParam(required = false) Integer hotelId, @RequestParam(required = false) Integer roomId, @RequestParam(required = false) Integer userId, @RequestParam(required = false) Integer amount, @RequestParam(required = false) Date start, @RequestParam(required = false) Date end) {
        try {
            log.info("Start getListBill with request: {}, {}, {}", page, pageSize, hotelId);
            Map<String, Object> result = billService.listBill(page, pageSize, hotelId, roomId, userId, amount, start, end);
            log.info("End getListBill: {}", result);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error getListBill: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("calculate-bill/{id}")
    @PreAuthorize(Authority.ADMIN)
    public ResponseEntity<?> calculateBill(@PathVariable Integer id) {
        try {
            log.info("Start calculateBill with request: {}", id);
            ResponseMessage responseMessage = billService.calculateBill(id);

            log.info("End calculateBill: {}", responseMessage);
            if (responseMessage.isStatus()) return ResponseEntity.ok(responseMessage);
            return ResponseEntity.badRequest().body(responseMessage);
        } catch (Exception e) {
            log.error("Error calculateBill: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}

