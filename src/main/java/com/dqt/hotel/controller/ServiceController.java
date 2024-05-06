package com.dqt.hotel.controller;

import com.dqt.hotel.constant.Authority;
import com.dqt.hotel.constant.Constant;
import com.dqt.hotel.dto.request.ServiceRequest;
import com.dqt.hotel.dto.response.ResponseMessage;
import com.dqt.hotel.service.ServicesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("${api.prefix}/service")

@Slf4j
@RequiredArgsConstructor
public class ServiceController {

    private final ServicesService serviceService;

    @GetMapping
    public ResponseEntity<?> getListService(@RequestParam(required = false, defaultValue = "1") Integer page,
                                         @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                                         @RequestParam(required = false) String key,
                                         @RequestParam(required = false) Integer hotelId
    ) {
        try {
            log.info("Start getListService with request: {}, {}, {}, {}", page, pageSize, key, hotelId);
            Map<String, Object> result = serviceService.listService(page, pageSize, key, hotelId);
            log.info("End getListService: {}", result);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error getListService: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    @PreAuthorize(Authority.ADMIN)
    public ResponseEntity<?> addService(@RequestBody ServiceRequest request) {
        try {
            log.info("Start addService with request: {}", request);
            ResponseMessage responseMessage = serviceService.addService(request);
            log.info("End addService: {}", responseMessage);
            if (responseMessage.isStatus()) return ResponseEntity.ok(responseMessage);
            return ResponseEntity.badRequest().body(responseMessage);
        } catch (Exception e) {
            log.error("Error addService: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @PreAuthorize(Authority.ADMIN)
    public ResponseEntity<?> editService(@RequestBody ServiceRequest request, @PathVariable Integer id) {
        try {
            log.info("Start editService with request: {}, {}", request, id);
            ResponseMessage responseMessage = serviceService.editService(request, id);
            log.info("End editService: {}", request);
            if (responseMessage.isStatus()) return ResponseEntity.ok(responseMessage);
            return ResponseEntity.badRequest().body(responseMessage);
        } catch (Exception e) {
            log.error("Error editService: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("{id}")
//    @PreAuthorize("permitAll()")
//    @PreAuthorize("!isAnonymous()")
    public ResponseEntity<?> getServiceById(@PathVariable Integer id) {
        try {
            log.info("Start getServiceById with request: {}", id);
            ResponseMessage responseMessage = serviceService.getServiceEnabledById(id);
            log.info("End getServiceById: {}", responseMessage);
            if (responseMessage.isStatus()) return ResponseEntity.ok(responseMessage);
            return ResponseEntity.badRequest().body(responseMessage);
        } catch (Exception e) {
            log.error("Error getServiceById: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("enable/{id}")
    @PreAuthorize(Authority.ADMIN)
    public ResponseEntity<?> enableService(@PathVariable Integer id) {
        try {
            log.info("Start enableService with request: {}", id);
            ResponseMessage responseMessage = serviceService.enableService(id, Constant.ACTIVE);
            log.info("End enableService: {}", responseMessage);
            if (responseMessage.isStatus()) return ResponseEntity.ok(responseMessage);
            return ResponseEntity.badRequest().body(responseMessage);
        } catch (Exception e) {
            log.error("Error enableService: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("disable/{id}")
    @PreAuthorize(Authority.ADMIN)
    public ResponseEntity<?> disableService(@PathVariable Integer id) {
        try {
            log.info("Start disableService with request: {}", id);
            ResponseMessage responseMessage = serviceService.enableService(id, Constant.INACTIVE);
            log.info("End disableService: {}", responseMessage);
            if (responseMessage.isStatus()) return ResponseEntity.ok(responseMessage);
            return ResponseEntity.badRequest().body(responseMessage);
        } catch (Exception e) {
            log.error("Error disableService: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
