package com.dqt.hotel.service;

import com.dqt.hotel.constant.Constant;
import com.dqt.hotel.dto.request.HotelRequest;
import com.dqt.hotel.dto.response.ResponseMessage;
import com.dqt.hotel.entity.Hotel;
import com.dqt.hotel.repository.HotelRepository;
import com.dqt.hotel.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class HotelService {

    private final HotelRepository hotelRepository;
    private final Utils utils;


    public Map<String, Object> listHotel(Integer page, Integer pageSize, String key) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, Constant.CREATED_DATE));
        Page<Hotel> all = hotelRepository.findAll(pageable, key);
        List<Hotel> all1 = hotelRepository.findAll1(key);
        Long count = hotelRepository.countTotal(key);
        Map<String, Object> stringObjectMap = Utils.putData(page, pageSize, count, all1);
        return stringObjectMap;
    }


    public ResponseMessage addHotel(HotelRequest hotelRequest) {
//        valid
        ResponseMessage responseMessage = validHotel(hotelRequest);
        if (!responseMessage.isStatus()) {
            return responseMessage;
        }

//        add
        Hotel hotel = Hotel.builder()
                .hotelName(hotelRequest.getHotelName())
                .address(hotelRequest.getAddress())
                .description(hotelRequest.getDescription())
                .enabled(Constant.ACTIVE)
                .createdBy(utils.gerCurrentUser().getId().toString())
                .createdDate(new Date()).build();

        Hotel save = hotelRepository.save(hotel);
        return ResponseMessage.ok("Add hotel successfully", save);
    }

    public ResponseMessage validHotel(HotelRequest hotelRequest) {
        if (hotelRequest.getHotelName().isBlank()) {
            return ResponseMessage.error("Please hotel name is blank");
        }
        if (hotelRequest.getAddress().isBlank()) {
            return ResponseMessage.error("Please hotel address is blank");
        }
        return ResponseMessage.ok("Validate successfully");
    }


    public ResponseMessage editHotel(HotelRequest request, Integer id) {
        Optional<Hotel> optional = hotelRepository.findById(id);
        if (!optional.isPresent()) {
            return ResponseMessage.error("Could not found by id: " + id);
        }
        Hotel hotel = optional.get();
        if (hotel.getEnabled().equals(Constant.INACTIVE)) {
            return ResponseMessage.error("Hotel is disabled: " + id);
        }
//        valid
        ResponseMessage responseMessage = validHotel(request);
        if (!responseMessage.isStatus()) {
            return responseMessage;
        }

//        edit
        BeanUtils.copyProperties(request, hotel);
        hotel.setModifiedBy(utils.gerCurrentUser().getId().toString());
        hotel.setModifiedDate(new Date());
        Hotel save = hotelRepository.save(hotel);
        return ResponseMessage.ok("Edit hotel successfully", save);
    }

    public ResponseMessage getHotelById(Integer id) {
        Optional<Hotel> optional = hotelRepository.findById(id);
        if (!optional.isPresent()) {
            return ResponseMessage.error("Could not found by id: " + id);
        }
        Hotel hotel = optional.get();
        if (hotel.getEnabled().equals(Constant.INACTIVE)) {
            return ResponseMessage.error("Hotel is disabled: " + id);
        }
        return ResponseMessage.ok("Get Hotel Successfully", hotel);
    }

    public ResponseMessage enableHotel(Integer id, Integer enabled) {
        Optional<Hotel> optional = hotelRepository.findById(id);
        if (!optional.isPresent()) {
            return ResponseMessage.error("Could not found by id: " + id);
        }
        String userId = utils.gerCurrentUser().getId().toString();
        hotelRepository.updateEnabled(enabled, userId, id);
        return ResponseMessage.ok("Successfully");
    }


}
