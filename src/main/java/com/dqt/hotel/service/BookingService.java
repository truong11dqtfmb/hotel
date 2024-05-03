package com.dqt.hotel.service;

import com.dqt.hotel.constant.Constant;
import com.dqt.hotel.dto.request.BookingRequest;
import com.dqt.hotel.dto.response.BookingResponse;
import com.dqt.hotel.dto.response.ResponseMessage;
import com.dqt.hotel.entity.Booking;
import com.dqt.hotel.entity.Room;
import com.dqt.hotel.repository.BookingRepository;
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
public class BookingService {

    private final HotelService hotelService;
    private final RoomRepository roomRepository;
    private final Utils utils;
    private final BookingRepository bookingRepository;
    private final RoomService roomService;

    public ResponseMessage addBooking(BookingRequest request) {
//        valid
        ResponseMessage responseMessage = validBooking(request);
        if (!responseMessage.isStatus()) return responseMessage;

        ResponseMessage checkStatusRoom = checkStatusRoom(request.getRoomId());
        if (!checkStatusRoom.isStatus()) return checkStatusRoom;

        Integer userId = utils.gerCurrentUser().getId();

//        add
        Booking booking = Booking.builder()
                .hotelId(request.getHotelId())
                .description(request.getDescription())
                .checkInDate(request.getCheckInDate())
                .checkOutDate(request.getCheckOutDate())
                .roomId(request.getRoomId())
                .userId(userId)
                .createdBy(userId.toString())
                .createdDate(new Date()).build();

        Booking save = bookingRepository.save(booking);

        roomService.updateStatusRoom(booking.getRoomId(), Constant.ROOM_FULL);
        return ResponseMessage.ok("Add booking successfully", save);
    }

    ResponseMessage checkStatusRoom(Integer roomId){
        if(Objects.isNull(roomId)){
            return ResponseMessage.ok("RoomId not found");
        }
        Optional<Room> optional = roomRepository.findById(roomId);

        if(!optional.isPresent()) return ResponseMessage.error("Room not found");
        Room room = optional.get();
        if(room.getStatus().equals(Constant.ROOM_FULL)){
            return ResponseMessage.error("Room is Full");
        }
        return ResponseMessage.ok("Room is empty");
    }


    public ResponseMessage editBooking(BookingRequest request, Integer id) {
//        valid
        Optional<Booking> optional = bookingRepository.findById(id);
        if (!optional.isPresent()) {
            return ResponseMessage.error("Booking not found");
        }
        ResponseMessage checkStatusRoom = checkStatusRoom(request.getRoomId());
        if (!checkStatusRoom.isStatus()) return checkStatusRoom;

        Integer userId = utils.gerCurrentUser().getId();

        Booking booking = optional.get();
        BeanUtils.copyProperties(request, booking);
        booking.setModifiedBy(userId.toString());
        booking.setModifiedDate(new Date());

        Booking save = bookingRepository.save(booking);

        roomService.updateStatusRoom(booking.getRoomId(), Constant.ROOM_FULL);
        return ResponseMessage.ok("Edit booking successfully", save);
    }

    private ResponseMessage validBooking(BookingRequest request) {
        if (Objects.isNull(request.getHotelId())) {
            return ResponseMessage.error("Please hotel is null");
        } else {
            ResponseMessage msg = hotelService.getHotelEnabledById(request.getHotelId());
            if (!msg.isStatus()) return msg;
        }
        if (!Objects.isNull(request.getRoomId())) {
            Optional<Room> optional = roomRepository.findById(request.getRoomId());
            if (!optional.isPresent()) return ResponseMessage.error("Could not found by id: " + request.getRoomId());
            Room room = optional.get();
            if (room.getEnabled().equals(Constant.INACTIVE))
                return ResponseMessage.error("Room is disabled: " + request.getRoomId());
        }
        return ResponseMessage.ok("Validate successfully");
    }

    public ResponseMessage getBooking(Integer id) {
        //        valid
        Optional<Booking> optional = bookingRepository.findById(id);
        if (!optional.isPresent()) {
            return ResponseMessage.error("Booking not found");
        }
        return ResponseMessage.ok("Get Booking Successfully", optional.get());
    }

    public ResponseMessage getBookingByUser(Integer userId) {
        List<Booking> allByUserId = bookingRepository.findAllByUserId(userId);
        return ResponseMessage.ok("Get Booking Successfully", allByUserId);
    }

    public Map<String, Object> listBooking(Integer page, Integer pageSize, Integer hotelId, Integer roomId, Integer userId, java.sql.Date start, java.sql.Date end) {
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.DESC, Constant.CREATED_DATE));
        List<BookingResponse> allResponses = bookingRepository.filterBooking(pageable, hotelId, roomId, userId, start, end);
        Long count = bookingRepository.countBooking(hotelId, roomId, userId, start, end);

        Map<String, Object> stringObjectMap = Utils.putData(page, pageSize, count, allResponses);
        return stringObjectMap;
    }


}
