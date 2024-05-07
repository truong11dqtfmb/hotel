package com.dqt.hotel.controller;

import com.dqt.hotel.constant.Authority;
import com.dqt.hotel.dto.response.ResponseMessage;
import com.dqt.hotel.service.AvatarService;
import com.dqt.hotel.utils.FileUtil;
import com.dqt.hotel.utils.MinIOUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("${api.prefix}/upload")

@Slf4j
@RequiredArgsConstructor
public class UploadController {

    @Value("${minio.bucket}")
    private String bucketName;

    private final MinIOUtils minIOUtils;

    private final AvatarService avatarService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize(Authority.ADMIN)
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file, @RequestParam Integer type, @RequestParam Integer id) throws IOException {
        try {
            log.info("Start uploadImage with request: {}, {}", type, id);
            ResponseMessage responseMessage = avatarService.uploadAvatar(file, type, id);
            log.info("End uploadImage: {}", responseMessage);
            if (responseMessage.isStatus()) return ResponseEntity.ok(responseMessage);
            return ResponseEntity.badRequest().body(responseMessage);
        } catch (Exception e) {
            log.error("Error uploadImage: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize(Authority.ADMIN)
    public ResponseEntity<?> updateImage(@RequestParam("file") MultipartFile file, @RequestParam Integer id) throws IOException {
        try {
            log.info("Start updateImage with request: {}", id);
            ResponseMessage responseMessage = avatarService.updateAvatar(file, id);
            log.info("End updateImage: {}", responseMessage);
            if (responseMessage.isStatus()) return ResponseEntity.ok(responseMessage);
            return ResponseEntity.badRequest().body(responseMessage);
        } catch (Exception e) {
            log.error("Error updateImage: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getAvatar(@PathVariable Integer id) {
        try {
            log.info("Start getAvatar with request: {}",id);
            ResponseMessage responseMessage = avatarService.getAvatarById(id);
            log.info("End getAvatar: {}", responseMessage);
            if (responseMessage.isStatus()) return ResponseEntity.ok(responseMessage);
            return ResponseEntity.badRequest().body(responseMessage);
        } catch (Exception e) {
            log.error("Error getAvatar: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @PreAuthorize(Authority.ADMIN)
    public ResponseEntity<?> deleteAvatar(@PathVariable Integer id) {
        try {
            log.info("Start deleteAvatar with request: {}",id);
            ResponseMessage responseMessage = avatarService.deleteAvatar(id);
            log.info("End deleteAvatar: {}", responseMessage);
            if (responseMessage.isStatus()) return ResponseEntity.ok(responseMessage);
            return ResponseEntity.badRequest().body(responseMessage);
        } catch (Exception e) {
            log.error("Error deleteAvatar: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("getImage/{photo}")
    public ResponseEntity<?> getImage(@PathVariable("photo") String photo) {
        log.info("Start getImage with request: {}", photo);
        if (!photo.equals("") || photo != null) {
            try {
                InputStream stream = minIOUtils.getObject(bucketName, photo);
                byte[] bytes = FileUtil.convertInputStreamToByteArray(stream);
                InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(bytes));

                return ResponseEntity.ok()
                        .contentLength(bytes.length)
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } catch (Exception e) {
                log.error("Error getImage: {}", e.getMessage());
                e.printStackTrace();
            }
        }
        return ResponseEntity.badRequest().build();
    }


}


