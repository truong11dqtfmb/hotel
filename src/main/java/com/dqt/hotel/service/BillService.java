package com.dqt.hotel.service;

import com.dqt.hotel.constant.Constant;
import com.dqt.hotel.dto.response.BillResponse;
import com.dqt.hotel.dto.response.ResponseMessage;
import com.dqt.hotel.entity.Bill;
import com.dqt.hotel.entity.Booking;
import com.dqt.hotel.entity.Room;
import com.dqt.hotel.repository.BillRepository;
import com.dqt.hotel.repository.BookingRepository;
import com.dqt.hotel.repository.RoomRepository;
import com.dqt.hotel.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BillService {

    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final BillRepository billRepository;
    private final RoomService roomService;

    public ResponseMessage calculateBill(Integer id) {
        //        valid
        Optional<Booking> optional = bookingRepository.findById(id);
        if (!optional.isPresent()) {
            return ResponseMessage.error("Booking not found");
        }
        Booking booking = optional.get();
        if (Objects.isNull(booking.getRoomId())) {
            return ResponseMessage.error("Room id is null");
        }

        Optional<Room> optionalRoom = roomRepository.findById(booking.getRoomId());
        if (!optionalRoom.isPresent()) {
            return ResponseMessage.error("Room not found");
        }

        Room room = optionalRoom.get();

        Integer diffDate = Utils.getDiffDate(booking.getCheckInDate(), booking.getCheckOutDate());
        Integer price = room.getPrice();

        Bill bill = billRepository.findBillByBookingId(id);
        if (Objects.isNull(bill)) {
            bill = new Bill();
        }
        bill.setBookingId(id);
        bill.setAmount(diffDate * price);
        bill.setPaymentDate(new Date());
        Bill save = billRepository.save(bill);

        roomService.updateStatusRoom(room.getId(), Constant.ROOM_EMPTY);

        return ResponseMessage.ok("Calculate bill successfully", save);
    }


    public Map<String, Object> listBill(Integer page, Integer pageSize, Integer hotelId, Integer roomId, Integer userId, Integer amount, java.sql.Date start, java.sql.Date end) {
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.DESC, Constant.PAYMENT_DATE));
        List<BillResponse> allResponses = billRepository.filterBill(pageable, hotelId, roomId, userId, amount, start, end);
        Long count = billRepository.countBill(hotelId, roomId, userId, amount, start, end);

        Map<String, Object> stringObjectMap = Utils.putData(page, pageSize, count, allResponses);
        return stringObjectMap;
    }

}
