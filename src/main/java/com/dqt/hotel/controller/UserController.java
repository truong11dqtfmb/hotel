package com.dqt.hotel.controller;

import  com.dqt.hotel.constant.Authority;
import  com.dqt.hotel.dto.request.RoleRequest;
import  com.dqt.hotel.dto.response.ResponseMessage;
import  com.dqt.hotel.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("${api.prefix}/user")

@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("change-role")
    @PreAuthorize(Authority.ADMIN)
    public ResponseEntity<?> changeRole(@RequestBody RoleRequest roleRequest) {
        try {
            ResponseMessage responseMessage = userService.changeRole(roleRequest);
            if (responseMessage.isStatus()) {
                return new ResponseEntity<>(responseMessage, HttpStatus.OK);
            }
            return ResponseEntity.badRequest().body(responseMessage);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("current-user")
    public ResponseEntity<?> getCurrentUser() {
        try {
            ResponseMessage responseMessage = userService.getCurrentUser();
            if (responseMessage.isStatus()) {
                return new ResponseEntity<>(responseMessage, HttpStatus.OK);
            }
            return ResponseEntity.badRequest().body(responseMessage);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
