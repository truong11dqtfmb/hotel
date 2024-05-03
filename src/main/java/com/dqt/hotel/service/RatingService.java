package com.dqt.hotel.service;

import com.dqt.hotel.constant.Constant;
import com.dqt.hotel.dto.request.RatingRequest;
import com.dqt.hotel.dto.response.ResponseMessage;
import com.dqt.hotel.entity.Booking;
import com.dqt.hotel.entity.Hotel;
import com.dqt.hotel.entity.Rating;
import com.dqt.hotel.repository.BookingRepository;
import com.dqt.hotel.repository.HotelRepository;
import com.dqt.hotel.repository.RatingRepository;
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
public class RatingService {

    private final BookingRepository bookingRepository;
    private final Utils utils;
    private final HotelRepository hotelRepository;
    private final RatingRepository ratingRepository;


    public ResponseMessage ratingHotel(RatingRequest request, Integer id) {
        Optional<Hotel> optional = hotelRepository.findById(id);
        if (!optional.isPresent()) return ResponseMessage.error("Could not found by id: " + id);
        Hotel hotel = optional.get();
        if (hotel.getEnabled().equals(Constant.INACTIVE)) {
            return ResponseMessage.error("Hotel is disabled: " + id);
        }
        Integer userId = utils.gerCurrentUser().getId();

        List<Rating> byUserId = ratingRepository.findByUserId(userId);
        if (!byUserId.isEmpty()) return ResponseMessage.error("You have already rated");

        Rating rating = Rating.builder()
                .hotelId(id)
                .description(request.getDescription())
                .rate(request.getRate())
                .createdBy(userId.toString())
                .createdDate(new Date())
                .build();

        Rating save = ratingRepository.save(rating);
        return ResponseMessage.ok("Thank you rated for Hotel: " + hotel.getHotelName(), save);
    }

    public ResponseMessage checkRating(Integer id) {
        Integer userId = utils.gerCurrentUser().getId();

        List<Booking> bookings = bookingRepository.checkRating(id, userId);

        if (bookings.isEmpty()) {
            return ResponseMessage.error("You don't have rating for Hotel");
        }
        List<Rating> byUserId = ratingRepository.findByUserId(userId);
        if (!byUserId.isEmpty()) return ResponseMessage.error("You have already rated");

        return ResponseMessage.ok("You can rating for Hotel");
    }

    public ResponseMessage calculateRatingHotel(Integer id) {
        Optional<Hotel> optional = hotelRepository.findById(id);
        if (!optional.isPresent()) return ResponseMessage.error("Could not found by id: " + id);
        Hotel hotel = optional.get();
        if (hotel.getEnabled().equals(Constant.INACTIVE)) {
            return ResponseMessage.error("Hotel is disabled: " + id);
        }
        List<Rating> allByHotelId = ratingRepository.findAllByHotelId(id);

        OptionalDouble average = allByHotelId.stream().map(Rating::getRate).mapToInt(a -> a).average();
        if (average.isPresent()) {
            return ResponseMessage.ok("Average rating for Hotel: " + hotel.getHotelName(), average.getAsDouble());
        }
        return ResponseMessage.ok("Average rating for Hotel: " + hotel.getHotelName(), 0);
    }

    public ResponseMessage deleteRating(Integer id) {
        Optional<Rating> optional = ratingRepository.findById(id);
        if (!optional.isPresent()) return ResponseMessage.error("Could not found by id: " + id);
        ratingRepository.deleteById(id);
        return ResponseMessage.ok("Rating deleted: " + id);
    }

    public Map<String, Object> getListRating(Integer page, Integer pageSize, Integer hotelId, Integer rate) {
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.DESC, Constant.CREATED_DATE));
        List<Rating> ratings = ratingRepository.filterRating(pageable, hotelId, rate);
        Long count = ratingRepository.countRating(hotelId, rate);

        Map<String, Object> stringObjectMap = Utils.putData(page, pageSize, count, ratings);
        return stringObjectMap;
    }
}
