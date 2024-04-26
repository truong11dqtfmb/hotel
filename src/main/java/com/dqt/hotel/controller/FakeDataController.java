package com.dqt.hotel.controller;

import com.dqt.hotel.constant.Authority;
import com.dqt.hotel.dto.response.ResponseMessage;
import com.dqt.hotel.service.FakeDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("${api.prefix}/fake-data")

@Slf4j
@RequiredArgsConstructor
public class FakeDataController {

    private final FakeDataService fakeDataService;


    @GetMapping("room")
    @PreAuthorize(Authority.ADMIN)
    public ResponseEntity<?> fakeDataRoom(@RequestParam(required = false, defaultValue = "1") Integer hotelId,
                                         @RequestParam(required = false, defaultValue = "50") Integer quantity
    ) {
        try {
            log.info("Start fakeDataHotel with request: {}, {}",hotelId, quantity);
            ResponseMessage responseMessage = fakeDataService.fakeDataRoom(hotelId, quantity);
            log.info("End fakeDataHotel: {}", responseMessage);
            return ResponseEntity.ok(responseMessage);
        } catch (Exception e) {
            log.error("Error fakeDataHotel: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("service")
    @PreAuthorize(Authority.ADMIN)
    public ResponseEntity<?> fakeDataService(@RequestParam(required = false, defaultValue = "1") Integer hotelId,
                                          @RequestParam(required = false, defaultValue = "50") Integer quantity
    ) {
        try {
            log.info("Start fakeDataService with request: {}, {}",hotelId, quantity);
            ResponseMessage responseMessage = fakeDataService.fakeDataService(hotelId, quantity);
            log.info("End fakeDataService: {}", responseMessage);
            return ResponseEntity.ok(responseMessage);
        } catch (Exception e) {
            log.error("Error fakeDataService: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
