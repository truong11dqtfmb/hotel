package com.dqt.hotel.controller;

import com.dqt.hotel.constant.Authority;
import com.dqt.hotel.constant.Constant;
import com.dqt.hotel.dto.request.HotelRequest;
import com.dqt.hotel.dto.response.ResponseMessage;
import com.dqt.hotel.service.HotelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.Map;


@RestController
@RequestMapping("${api.prefix}/hotel")

@Slf4j
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    @GetMapping("home")
    public ResponseEntity<?> getHomeList(@RequestParam(required = false, defaultValue = "1") Integer page,
                                         @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                                         @RequestParam(required = false) String address,
                                         @RequestParam(required = false) Date checkIn,
                                         @RequestParam(required = false) Date checkOut,
                                         @RequestParam(required = false) Integer member
    ) {
        try {
            log.info("Start getHomeList with request: {}, {}, {}", page, pageSize);
            Map<String, Object> result = hotelService.getHomeList(page, pageSize, address, checkIn, checkOut, member);
            log.info("End getHomeList: {}", result);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error getHomeList: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getListHotel(@RequestParam(required = false, defaultValue = "1") Integer page,
                                          @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                                          @RequestParam(required = false) String key) {
        try {
            log.info("Start getListHotel with request: {}, {}, {}", page, pageSize, key);
            Map<String, Object> result = hotelService.listHotel(page, pageSize, key);
            log.info("End getListHotel: {}", result);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error getListHotel: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("address-hotel")
    public ResponseEntity<?> getListAddressHotel() {
        try {
            log.info("Start getListAddressHotel with request: ");
            ResponseMessage responseMessage = hotelService.getListAddressHotel();
            log.info("End getListAddressHotel: {}", responseMessage);
            if (responseMessage.isStatus()) {
                return ResponseEntity.ok(responseMessage);
            }
            return ResponseEntity.badRequest().body(responseMessage);
        } catch (Exception e) {
            log.error("Error getListAddressHotel: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    @PreAuthorize(Authority.ADMIN)
    public ResponseEntity<?> addHotel(@RequestBody HotelRequest request) {
        try {
            log.info("Start addHotel with request: {}", request);
            ResponseMessage responseMessage = hotelService.addHotel(request);
            log.info("End addHotel: {}", responseMessage);
            if (responseMessage.isStatus()) {
                return ResponseEntity.ok(responseMessage);
            }
            return ResponseEntity.badRequest().body(responseMessage);
        } catch (Exception e) {
            log.error("Error addHotel: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @PreAuthorize(Authority.ADMIN)
    public ResponseEntity<?> editHotel(@RequestBody HotelRequest request, @PathVariable Integer id) {
        try {
            log.info("Start editHotel with request: {}, {}", request, id);
            ResponseMessage responseMessage = hotelService.editHotel(request, id);
            log.info("End editHotel: {}", request);
            if (responseMessage.isStatus()) {
                return ResponseEntity.ok(responseMessage);
            }
            return ResponseEntity.badRequest().body(responseMessage);
        } catch (Exception e) {
            log.error("Error editHotel: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("{id}")
//    @PreAuthorize("permitAll()")
//    @PreAuthorize("!isAnonymous()")
    public ResponseEntity<?> getHotelById(@PathVariable Integer id) {
        try {
            log.info("Start getHotelById with request: {}", id);
            ResponseMessage responseMessage = hotelService.getHotelEnabledById(id);
            log.info("End getHotelById: {}", responseMessage);
            if (responseMessage.isStatus()) {
                return ResponseEntity.ok(responseMessage);
            }
            return ResponseEntity.badRequest().body(responseMessage);
        } catch (Exception e) {
            log.error("Error getHotelById: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("detail/{id}")
//    @PreAuthorize("permitAll()")
//    @PreAuthorize("!isAnonymous()")
    public ResponseEntity<?> getHotelDetailById(@PathVariable Integer id) {
        try {
            log.info("Start getHotelDetailById with request: {}", id);
            ResponseMessage responseMessage = hotelService.getHotelDetailById(id);
            log.info("End getHotelDetailById: {}", responseMessage);
            if (responseMessage.isStatus()) {
                return ResponseEntity.ok(responseMessage);
            }
            return ResponseEntity.badRequest().body(responseMessage);
        } catch (Exception e) {
            log.error("Error getHotelDetailById: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("enable/{id}")
    @PreAuthorize(Authority.ADMIN)
    public ResponseEntity<?> enableHotel(@PathVariable Integer id) {
        try {
            log.info("Start enableHotel with request: {}", id);
            ResponseMessage responseMessage = hotelService.enableHotel(id, Constant.ACTIVE);
            log.info("End enableHotel: {}", responseMessage);
            if (responseMessage.isStatus()) {
                return ResponseEntity.ok(responseMessage);
            }
            return ResponseEntity.badRequest().body(responseMessage);
        } catch (Exception e) {
            log.error("Error enableHotel: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("disable/{id}")
    @PreAuthorize(Authority.ADMIN)
    public ResponseEntity<?> disableHotel(@PathVariable Integer id) {
        try {
            log.info("Start disableHotel with request: {}", id);
            ResponseMessage responseMessage = hotelService.enableHotel(id, Constant.INACTIVE);
            log.info("End disableHotel: {}", responseMessage);
            if (responseMessage.isStatus()) {
                return ResponseEntity.ok(responseMessage);
            }
            return ResponseEntity.badRequest().body(responseMessage);
        } catch (Exception e) {
            log.error("Error disableHotel: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
