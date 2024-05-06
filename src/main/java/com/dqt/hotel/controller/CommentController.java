package com.dqt.hotel.controller;

import com.dqt.hotel.constant.Authority;
import com.dqt.hotel.dto.request.CommentRequest;
import com.dqt.hotel.dto.response.ResponseMessage;
import com.dqt.hotel.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/comment")

@Slf4j
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<?> getListComment(@RequestParam(required = false, defaultValue = "1") Integer page,
                                            @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                                            @RequestParam(required = false) Integer commentId,
                                            @RequestParam(required = false) Integer type,
                                            @RequestParam(required = false) String key) {
        try {
            log.info("Start getListComment with request: {}, {}, {}", type, commentId, key);
            Map<String, Object> result = commentService.getListComment(page, pageSize, type, commentId, key);
            log.info("End getListComment: {}", result);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error getListComment: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> commentHotel(@RequestBody CommentRequest request) {
        try {
            log.info("Start commentHotel with request: {}", request);
            ResponseMessage responseMessage = commentService.commentHotel(request);

            log.info("End commentHotel: {}", responseMessage);
            if (responseMessage.isStatus()) return ResponseEntity.ok(responseMessage);
            return ResponseEntity.badRequest().body(responseMessage);
        } catch (Exception e) {
            log.error("Error commentHotel: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @PreAuthorize(Authority.ADMIN)
    public ResponseEntity<?> deleteComment(@PathVariable Integer id) {
        try {
            log.info("Start deleteComment with request: {}", id);
            ResponseMessage responseMessage = commentService.deleteComment(id);

            log.info("End deleteComment: {}", responseMessage);
            if (responseMessage.isStatus()) return ResponseEntity.ok(responseMessage);
            return ResponseEntity.badRequest().body(responseMessage);
        } catch (Exception e) {
            log.error("Error deleteComment: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}

