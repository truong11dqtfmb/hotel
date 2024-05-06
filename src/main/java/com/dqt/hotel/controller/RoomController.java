package com.dqt.hotel.controller;

import com.dqt.hotel.constant.Authority;
import com.dqt.hotel.constant.Constant;
import com.dqt.hotel.dto.request.RoomRequest;
import com.dqt.hotel.dto.response.ResponseMessage;
import com.dqt.hotel.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("${api.prefix}/room")

@Slf4j
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping
    public ResponseEntity<?> getListRoom(@RequestParam(required = false, defaultValue = "1") Integer page,
                                         @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                                         @RequestParam(required = false) String key,
                                         @RequestParam(required = false) Integer hotelId,
                                         @RequestParam(required = false) Integer acreage,
                                         @RequestParam(required = false) Integer member,
                                         @RequestParam(required = false) Integer priceMax,
                                         @RequestParam(required = false) Integer priceMin
    ) {
        try {
            log.info("Start getListRoom with request: {}, {}, {}, {}, {}, {}, {}, {}", page, pageSize, key, hotelId, acreage, member, priceMax, priceMin);
            Map<String, Object> result = roomService.listRoom(page, pageSize, key, hotelId, acreage, member, priceMax, priceMin);
            log.info("End getListRoom: {}", result);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error getListRoom: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    @PreAuthorize(Authority.ADMIN)
    public ResponseEntity<?> addRoom(@RequestBody RoomRequest request) {
        try {
            log.info("Start addRoom with request: {}", request);
            ResponseMessage responseMessage = roomService.addRoom(request);
            log.info("End addRoom: {}", responseMessage);
            if (responseMessage.isStatus()) return ResponseEntity.ok(responseMessage);
            return ResponseEntity.badRequest().body(responseMessage);
        } catch (Exception e) {
            log.error("Error addRoom: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @PreAuthorize(Authority.ADMIN)
    public ResponseEntity<?> editRoom(@RequestBody RoomRequest request, @PathVariable Integer id) {
        try {
            log.info("Start editRoom with request: {}, {}", request, id);
            ResponseMessage responseMessage = roomService.editRoom(request, id);
            log.info("End editRoom: {}", request);
            if (responseMessage.isStatus()) return ResponseEntity.ok(responseMessage);
            return ResponseEntity.badRequest().body(responseMessage);
        } catch (Exception e) {
            log.error("Error editRoom: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("{id}")
//    @PreAuthorize("permitAll()")
//    @PreAuthorize("!isAnonymous()")
    public ResponseEntity<?> getRoomById(@PathVariable Integer id) {
        try {
            log.info("Start getRoomById with request: {}", id);
            ResponseMessage responseMessage = roomService.getRoomEnabledById(id);
            log.info("End getRoomById: {}", responseMessage);
            if (responseMessage.isStatus()) return ResponseEntity.ok(responseMessage);
            return ResponseEntity.badRequest().body(responseMessage);
        } catch (Exception e) {
            log.error("Error getRoomById: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("enable/{id}")
    @PreAuthorize(Authority.ADMIN)
    public ResponseEntity<?> enableRoom(@PathVariable Integer id) {
        try {
            log.info("Start enableRoom with request: {}", id);
            ResponseMessage responseMessage = roomService.enableRoom(id, Constant.ACTIVE);
            log.info("End enableRoom: {}", responseMessage);
            if (responseMessage.isStatus()) return ResponseEntity.ok(responseMessage);
            return ResponseEntity.badRequest().body(responseMessage);
        } catch (Exception e) {
            log.error("Error enableRoom: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("disable/{id}")
    @PreAuthorize(Authority.ADMIN)
    public ResponseEntity<?> disableRoom(@PathVariable Integer id) {
        try {
            log.info("Start disableRoom with request: {}", id);
            ResponseMessage responseMessage = roomService.enableRoom(id, Constant.INACTIVE);
            log.info("End disableRoom: {}", responseMessage);
            if (responseMessage.isStatus()) return ResponseEntity.ok(responseMessage);
            return ResponseEntity.badRequest().body(responseMessage);
        } catch (Exception e) {
            log.error("Error disableRoom: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
