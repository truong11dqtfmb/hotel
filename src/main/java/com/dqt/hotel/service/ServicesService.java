package com.dqt.hotel.service;

import com.dqt.hotel.constant.Constant;
import com.dqt.hotel.dto.request.ServiceRequest;
import com.dqt.hotel.dto.response.ResponseMessage;
import com.dqt.hotel.dto.response.ServicesResponse;
import com.dqt.hotel.entity.Services;
import com.dqt.hotel.repository.ServicesRepository;
import com.dqt.hotel.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
public class ServicesService {

    private final ServicesRepository serviceRepository;
    private final HotelService hotelService;
    private final Utils utils;


    public Map<String, Object> listService(Integer page, Integer pageSize, String key, Integer hotelId) {
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.DESC, Constant.CREATED_DATE));
        List<ServicesResponse> all1 = serviceRepository.filterAllServiceResponse(pageable, Utils.checkNullString(key), hotelId);
        Long count = serviceRepository.countFilterService(Utils.checkNullString(key), hotelId);
        Map<String, Object> stringObjectMap = Utils.putData(page, pageSize, count, all1);

        return stringObjectMap;
    }

    public ResponseMessage addService(ServiceRequest request) {
//        valid
        ResponseMessage responseMessage = validService(request);
        if (!responseMessage.isStatus()) return responseMessage;

//        add
        Services service = Services.builder()
                .serviceName(request.getServiceName())
                .hotelId(request.getHotelId())
                .description(request.getDescription())
                .enabled(Constant.ACTIVE)
                .createdBy(utils.gerCurrentUser().getId().toString())
                .createdDate(new Date()).build();

        Services save = serviceRepository.save(service);
        return ResponseMessage.ok("Add hotel successfully", save);
    }

    public ResponseMessage editService(ServiceRequest request, Integer id) {
        ResponseMessage message = this.getServiceEnabledById(id);
        if (!message.isStatus()) return message;

//        valid
        ResponseMessage responseMessage = validService(request);
        if (!responseMessage.isStatus()) return responseMessage;

//        edit
        Services service = (Services) message.getData();
        BeanUtils.copyProperties(request, service);
        service.setModifiedBy(utils.gerCurrentUser().getId().toString());
        service.setModifiedDate(new Date());
        Services save = serviceRepository.save(service);
        return ResponseMessage.ok("Edit service successfully", save);
    }

    public ResponseMessage validService(ServiceRequest request) {
        ResponseMessage msg = hotelService.getHotelEnabledById(request.getHotelId());
        if (!msg.isStatus()) return msg;

        return ResponseMessage.ok("Validate successfully");
    }

    public ResponseMessage getServiceEnabledById(Integer id) {
        Optional<Services> optional = serviceRepository.findById(id);
        if (!optional.isPresent()) return ResponseMessage.error("Could not found by id: " + id);
        Services service = optional.get();
        if (service.getEnabled().equals(Constant.INACTIVE)) {
            return ResponseMessage.error("Service is disabled: " + id);
        }
        return ResponseMessage.ok("Get Service Enabled Successfully", service);
    }

    public ResponseMessage getServiceById(Integer id) {
        Optional<Services> optional = serviceRepository.findById(id);
        if (!optional.isPresent()) return ResponseMessage.error("Could not found by id: " + id);
        return ResponseMessage.ok("Get Service Successfully", optional.get());
    }

    public ResponseMessage enableService(Integer id, Integer enabled) {
        ResponseMessage message = getServiceById(id);
        if (!message.isStatus()) return message;
        String userId = utils.gerCurrentUser().getId().toString();
        serviceRepository.updateEnabled(enabled, userId, id);
        return ResponseMessage.ok("Successfully");
    }


}
