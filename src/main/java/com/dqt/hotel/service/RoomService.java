package com.dqt.hotel.service;

import com.dqt.hotel.constant.Constant;
import com.dqt.hotel.dto.request.RoomRequest;
import com.dqt.hotel.dto.response.ResponseMessage;
import com.dqt.hotel.dto.response.RoomResponse;
import com.dqt.hotel.entity.Room;
import com.dqt.hotel.repository.RoomRepository;
import com.dqt.hotel.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final HotelService hotelService;
    private final Utils utils;


    public Map<String, Object> listRoom(Integer page, Integer pageSize, String key, Integer hotelId, Integer acreage, Integer member, Integer priceMax, Integer priceMin) {
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.DESC, Constant.CREATED_DATE));
        List<RoomResponse> all = roomRepository.filterAllRoomResponse(pageable, Utils.checkNullString(key), hotelId, acreage, member, priceMax, priceMin);
        Long count = roomRepository.countFilterRoom(Utils.checkNullString(key), hotelId, acreage, member, priceMax, priceMin);
        Map<String, Object> stringObjectMap = Utils.putData(page, pageSize, count, all);

        return stringObjectMap;
    }

    public ResponseMessage addRoom(RoomRequest request) {
//        valid
        ResponseMessage responseMessage = validRoom(request);
        if (!responseMessage.isStatus()) return responseMessage;

//        add
        Room room = Room.builder()
                .roomName(request.getRoomName())
                .hotelId(request.getHotelId())
                .acreage(request.getAcreage())
                .member(request.getMember())
                .description(request.getDescription())
                .price(request.getPrice())
                .enabled(Constant.ACTIVE)
                .createdBy(utils.gerCurrentUser().getId().toString())
                .createdDate(new Date()).build();

        Room save = roomRepository.save(room);
        return ResponseMessage.ok("Add hotel successfully", save);
    }

    public ResponseMessage editRoom(RoomRequest request, Integer id) {
        ResponseMessage message = this.getRoomEnabledById(id);
        if (!message.isStatus()) return message;

//        valid
        ResponseMessage responseMessage = validRoom(request);
        if (!responseMessage.isStatus()) return responseMessage;

//        edit
        Room room = (Room) message.getData();
        BeanUtils.copyProperties(request, room);
        room.setModifiedBy(utils.gerCurrentUser().getId().toString());
        room.setModifiedDate(new Date());
        Room save = roomRepository.save(room);
        return ResponseMessage.ok("Edit room successfully", save);
    }

    public ResponseMessage validRoom(RoomRequest request) {
        if (request.getRoomName().isBlank()) {
            return ResponseMessage.error("Please room name is blank");
        }
        if (Objects.isNull(request.getAcreage())) {
            return ResponseMessage.error("Please room acreage is null");
        }
        if (Objects.isNull(request.getMember())) {
            return ResponseMessage.error("Please room member is null");
        }
        if (Objects.isNull(request.getPrice())) {
            return ResponseMessage.error("Please room price is null");
        }

        if (Objects.isNull(request.getHotelId())) {
            return ResponseMessage.error("Please hotel is null");
        } else {
            ResponseMessage msg = hotelService.getHotelEnabledById(request.getHotelId());
            if (!msg.isStatus()) return msg;
        }

        return ResponseMessage.ok("Validate successfully");
    }

    public ResponseMessage getRoomEnabledById(Integer id) {
        Optional<Room> optional = roomRepository.findById(id);
        if (!optional.isPresent()) return ResponseMessage.error("Could not found by id: " + id);
        Room room = optional.get();
        if (room.getEnabled().equals(Constant.INACTIVE)) return ResponseMessage.error("Room is disabled: " + id);
        return ResponseMessage.ok("Get Room Enabled Successfully", room);
    }

    public ResponseMessage getRoomById(Integer id) {
        Optional<Room> optional = roomRepository.findById(id);
        if (!optional.isPresent()) return ResponseMessage.error("Could not found by id: " + id);
        return ResponseMessage.ok("Get Room Successfully", optional.get());
    }

    public ResponseMessage enableRoom(Integer id, Integer enabled) {
        ResponseMessage message = getRoomById(id);
        if (!message.isStatus()) return message;
        String userId = utils.gerCurrentUser().getId().toString();
        roomRepository.updateEnabled(enabled, userId, id);
        return ResponseMessage.ok("Successfully");
    }


}
