package com.dqt.hotel.service;

import com.dqt.hotel.constant.Constant;
import com.dqt.hotel.dto.response.ResponseMessage;
import com.dqt.hotel.entity.Avatar;
import com.dqt.hotel.repository.AvatarRepository;
import com.dqt.hotel.repository.HotelRepository;
import com.dqt.hotel.repository.RoomRepository;
import com.dqt.hotel.utils.FileUtil;
import com.dqt.hotel.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AvatarService {
    private final AvatarRepository avatarRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final Utils utils;

    public ResponseMessage uploadAvatar(MultipartFile file, Integer type, Integer id) throws IOException {
        ResponseMessage responseMessage = validateAvatar(file, type, id);
        if (!responseMessage.isStatus()) return responseMessage;

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileCode = RandomStringUtils.randomAlphanumeric(8);
        String imageName = fileCode + "-" + fileName;
        FileUtil.saveFile(imageName, file);

        Avatar avatar = new Avatar();
        avatar.setCreatedDate(new Date());
        avatar.setCreatedBy(utils.gerCurrentUser().getId().toString());
        avatar.setType(type.toString());
        avatar.setImageId(id);
        avatar.setImageName(imageName);
        avatarRepository.save(avatar);
        return ResponseMessage.ok("Upload avatar success");
    }

    public ResponseMessage getAvatarById(Integer id) {
        Optional<Avatar> optional = avatarRepository.findById(id);
        if (!optional.isPresent()) return ResponseMessage.error("Couldn't find avatar");
        return ResponseMessage.ok("Get Avatar success", optional.get());

    }

    public ResponseMessage validateAvatar(MultipartFile file, Integer type, Integer id) {
        ResponseMessage responseMessage = validateFile(file);
        if (!responseMessage.isStatus()) return responseMessage;
        if (type.equals(Constant.TYPE_HOTEL)) {
            if (Objects.isNull(hotelRepository.findByIdAndEnabled(id, Constant.ACTIVE))) {
                return ResponseMessage.error("Hotel doesn't exist");
            }
        } else if (type.equals(Constant.TYPE_ROOM)) {
            if (Objects.isNull(roomRepository.findByIdAndEnabled(id, Constant.ACTIVE))) {
                return ResponseMessage.error("Room doesn't exist");
            }
        } else {
            return ResponseMessage.error("Type is invalid (1: hotel, 2: room");
        }
        return ResponseMessage.ok("Valid successfully");
    }

    public ResponseMessage validateFile(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (file.isEmpty()) {
            return ResponseMessage.error("File is not empty");
        } else if (file.getSize() > Constant.MAX_FILE_SIZE) {
            return ResponseMessage.error("File size is too large > 20mb");
        } else if (!isValidImageExtension(originalFilename)) {
            return ResponseMessage.error("File is must be image");
        }
        return ResponseMessage.ok("Valid successfully");
    }

    public static boolean isValidImageExtension(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        for (String allowedExtension : Constant.ALLOWED_EXTENSIONS) {
            if (extension.endsWith(allowedExtension)) {
                return true;
            }
        }
        return false;
    }

    public ResponseMessage deleteAvatar(Integer id) {
        Optional<Avatar> optional = avatarRepository.findById(id);
        if (!optional.isPresent()) return ResponseMessage.error("Couldn't find avatar");
        avatarRepository.deleteById(id);
        return ResponseMessage.ok("Delete Avatar success");
    }

    public ResponseMessage updateAvatar(MultipartFile file, Integer id) throws IOException {
        Optional<Avatar> optional = avatarRepository.findById(id);
        if (!optional.isPresent()) {
            return ResponseMessage.error("Couldn't find avatar");
        }

        ResponseMessage responseMessage = validateFile(file);
        if (!responseMessage.isStatus()) return responseMessage;

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileCode = RandomStringUtils.randomAlphanumeric(8);
        String imageName = fileCode + "-" + fileName;
        FileUtil.saveFile(imageName, file);

        Avatar avatar = optional.get();
        avatar.setModifiedDate(new Date());
        avatar.setModifiedBy(utils.gerCurrentUser().getId().toString());
        avatar.setImageName(imageName);
        avatarRepository.save(avatar);
        return ResponseMessage.ok("Update avatar success");
    }
}
