package com.dqt.hotel.service;

import com.dqt.hotel.constant.Constant;
import com.dqt.hotel.dto.request.HotelRequest;
import com.dqt.hotel.dto.response.HotelResponse;
import com.dqt.hotel.dto.response.ResponseMessage;
import com.dqt.hotel.entity.Avatar;
import com.dqt.hotel.entity.Hotel;
import com.dqt.hotel.entity.Room;
import com.dqt.hotel.entity.Services;
import com.dqt.hotel.repository.AvatarRepository;
import com.dqt.hotel.repository.HotelRepository;
import com.dqt.hotel.repository.RoomRepository;
import com.dqt.hotel.repository.ServicesRepository;
import com.dqt.hotel.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HotelService {

    private final HotelRepository hotelRepository;
    private final AvatarRepository avatarRepository;
    private final ServicesRepository serviceRepository;
    private final RoomRepository roomRepository;
    private final Utils utils;


    public Map<String, Object> listHotel(Integer page, Integer pageSize, String key) {
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.DESC, Constant.CREATED_DATE));
        List<Hotel> all = hotelRepository.findAll(pageable, Utils.checkNullString(key));

        List<HotelResponse> hotelResponses = convertListHotelResponse(all);
        Long count = hotelRepository.countTotal(Utils.checkNullString(key));
        Map<String, Object> stringObjectMap = Utils.putData(page, pageSize, count, hotelResponses);

        return stringObjectMap;
    }


    public ResponseMessage addHotel(HotelRequest hotelRequest) {
//        valid
        ResponseMessage responseMessage = validHotel(hotelRequest);
        if (!responseMessage.isStatus()) return responseMessage;

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
        ResponseMessage message = this.getHotelEnabledById(id);
        if (!message.isStatus()) return message;

//        valid
        ResponseMessage responseMessage = validHotel(request);
        if (!responseMessage.isStatus()) return responseMessage;

//        edit
        Hotel hotel = (Hotel) message.getData();
        BeanUtils.copyProperties(request, hotel);
        hotel.setModifiedBy(utils.gerCurrentUser().getId().toString());
        hotel.setModifiedDate(new Date());
        Hotel save = hotelRepository.save(hotel);
        return ResponseMessage.ok("Edit hotel successfully", save);
    }

    public ResponseMessage getHotelEnabledById(Integer id) {
        Optional<Hotel> optional = hotelRepository.findById(id);
        if (!optional.isPresent()) return ResponseMessage.error("Could not found by id: " + id);
        Hotel hotel = optional.get();
        if (hotel.getEnabled().equals(Constant.INACTIVE)) {
            return ResponseMessage.error("Hotel is disabled: " + id);
        }
        return ResponseMessage.ok("Get Hotel Enabled Successfully", hotel);
    }

    public ResponseMessage getHotelById(Integer id) {
        Optional<Hotel> optional = hotelRepository.findById(id);
        if (!optional.isPresent()) return ResponseMessage.error("Could not found by id: " + id);
        return ResponseMessage.ok("Get Hotel Successfully", optional.get());
    }

    public ResponseMessage enableHotel(Integer id, Integer enabled) {
        ResponseMessage message = this.getHotelById(id);
        if (!message.isStatus()) {
            return message;
        }
        String userId = utils.gerCurrentUser().getId().toString();
        hotelRepository.updateEnabled(enabled, userId, id);
        return ResponseMessage.ok("Successfully");
    }


    public ResponseMessage getHotelDetailById(Integer id) {
        Optional<Hotel> optional = hotelRepository.findById(id);
        if (!optional.isPresent()) return ResponseMessage.error("Could not found by id: " + id);
        Hotel hotel = optional.get();
        if (hotel.getEnabled().equals(Constant.INACTIVE)) {
            return ResponseMessage.error("Hotel is disabled: " + id);
        }
        HotelResponse hotelResponse = new HotelResponse();
        BeanUtils.copyProperties(hotel, hotelResponse);
        hotelResponse.setAvatar(avatarRepository.findAvatarById(id, Constant.TYPE_HOTEL));

        List<Services> services = serviceRepository.filterAllService("", id);
        hotelResponse.setServices(services);

        List<Room> rooms = roomRepository.findAllRoom("", id, null, null, null, null);
        hotelResponse.setRooms(rooms);

        return ResponseMessage.ok("Get Hotel Enabled Successfully", hotelResponse);

    }

    public ResponseMessage getListAddressHotel() {
        List<String> listAddressHotel = hotelRepository.getListAddressHotel();
        return ResponseMessage.ok("Get Address Hotel Successfully", listAddressHotel);
    }

    public Map<String, Object> getHomeList(Integer page, Integer pageSize, String address, java.sql.Date checkIn, java.sql.Date checkOut, Integer member) {
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.DESC, Constant.CREATED_DATE));
        List<Hotel> all = hotelRepository.findHomeHotel(pageable, Utils.checkNullString(address), member);
        Long count = hotelRepository.countHomeHotel(Utils.checkNullString(address), member);

        List<HotelResponse> hotelResponses = convertListHotelResponse(all);

        Map<String, Object> stringObjectMap = Utils.putData(page, pageSize, count, hotelResponses);
        return stringObjectMap;
    }

    public List<HotelResponse> convertListHotelResponse(List<Hotel> all){
        List<Integer> ids = all.stream().map(Hotel::getId).collect(Collectors.toList());
        List<Avatar> avatars = avatarRepository.findAvatarByIds(ids, Constant.TYPE_HOTEL);

        List<HotelResponse> hotelResponses = new ArrayList<>();
        all.forEach(h -> {
            HotelResponse hotelResponse = new HotelResponse();
            BeanUtils.copyProperties(h, hotelResponse);
            List<String> collect = avatars.stream().filter(a -> h.getId().equals(a.getImageId())).map(Avatar::getImageName).collect(Collectors.toList());
            hotelResponse.setAvatar(collect);
            hotelResponses.add(hotelResponse);
        });

        return hotelResponses;
    }


}
