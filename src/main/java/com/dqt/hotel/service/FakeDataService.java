package com.dqt.hotel.service;

import com.dqt.hotel.constant.Constant;
import com.dqt.hotel.dto.response.ResponseMessage;
import com.dqt.hotel.entity.Room;
import com.dqt.hotel.entity.Services;
import com.dqt.hotel.repository.RoomRepository;
import com.dqt.hotel.repository.ServicesRepository;
import com.dqt.hotel.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FakeDataService {

    private final RoomRepository roomRepository;
    private final ServicesRepository servicesRepository;

    Integer[] sourceAcreage = {18, 20, 23, 25, 30, 35};
    List<Integer> listAcreage = Arrays.asList(sourceAcreage);

    Integer[] sourceMember = {3, 4, 5, 6};
    List<Integer> listMember = Arrays.asList(sourceMember);

    Integer[] sourcePrice = {900000, 1000000, 1200000, 1300000, 1500000, 1800000};
    List<Integer> listPrice = Arrays.asList(sourcePrice);

    public ResponseMessage fakeDataRoom(Integer hotelId, Integer quantity) {
        List<Room> roomToAdd = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            Room room = new Room();
            room.setHotelId(hotelId);
            room.setDescription("Dep");
            room.setRoomName("Phong " + (i + 1));
            Integer randomAcreage = Utils.getRandomElement(listAcreage);
            room.setAcreage(randomAcreage);

            Integer randomMember = Utils.getRandomElement(listMember);
            room.setMember(randomMember);

            Integer randomPrice = Utils.getRandomElement(listPrice);
            room.setPrice(randomPrice);

            room.setEnabled(Constant.ACTIVE);
            room.setCreatedBy("dao quoc truong");
            room.setCreatedDate(new Date());
            roomToAdd.add(room);
        }
        roomRepository.saveAll(roomToAdd);
        return ResponseMessage.ok("Random room successfully added");
    }

    public ResponseMessage fakeDataService(Integer hotelId, Integer quantity) {
        List<Services> serviceToAdd = new ArrayList<>();
        String[] sourceServiceName = {
                "WiFi miễn phí",
                "Xe đưa đón sân bay",
                "Phòng gia đình",
                "Trung tâm thể dục",
                "Trung tâm Spa",
                "Chăm sóc sức khoẻ",
                "Phòng không hút thuốc",
                "Chỗ đỗ xe miễn phí",
                "Lễ tân 24 giờ",
                "Bữa sáng rất tốt",
                "Breakfast included for 1 pax",
                "Máy pha trà/cà phê trong tất cả các phòng"
        };
        List<String> listServiceName = Arrays.asList(sourceServiceName);
        for (int i = 0; i < listServiceName.size(); i++) {
            Services services = new Services();
            services.setHotelId(hotelId);
            services.setDescription("Rất tốt");
            services.setServiceName(listServiceName.get(i));
            services.setEnabled(Constant.ACTIVE);
            services.setCreatedBy("1");
            services.setCreatedDate(new Date());
            serviceToAdd.add(services);
        }
        servicesRepository.saveAll(serviceToAdd);
        return ResponseMessage.ok("Random room successfully added");
    }

}
