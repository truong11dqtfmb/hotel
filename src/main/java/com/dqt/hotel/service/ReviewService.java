package com.dqt.hotel.service;

import com.dqt.hotel.constant.Constant;
import com.dqt.hotel.dto.request.ReviewRequest;
import com.dqt.hotel.dto.response.ResponseMessage;
import com.dqt.hotel.entity.Hotel;
import com.dqt.hotel.entity.Review;
import com.dqt.hotel.repository.HotelRepository;
import com.dqt.hotel.repository.ReviewRepository;
import com.dqt.hotel.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class ReviewService {

    private final Utils utils;
    private final HotelRepository hotelRepository;
    private final ReviewRepository reviewRepository;

    public ResponseMessage reviewHotel(ReviewRequest request, Integer id) {
        Optional<Hotel> optional = hotelRepository.findById(id);
        if (!optional.isPresent()) return ResponseMessage.error("Could not found by id: " + id);
        Hotel hotel = optional.get();
        if (hotel.getEnabled().equals(Constant.INACTIVE))  return ResponseMessage.error("Hotel is disabled: " + id);
        Review review = Review.builder()
                .hotelId(id)
                .title(request.getTitle())
                .content(request.getContent())
                .createdBy(utils.gerCurrentUser().getId().toString())
                .createdDate(new Date())
                .build();

        Review save = reviewRepository.save(review);
        return ResponseMessage.ok("Thank you review for Hotel: " + hotel.getHotelName(), save);
    }

    public ResponseMessage deleteReview(Integer id) {
        Optional<Review> optional = reviewRepository.findById(id);
        if (!optional.isPresent()) return ResponseMessage.error("Could not found by id: " + id);
        reviewRepository.deleteById(id);
        return ResponseMessage.ok("Review deleted: " + id);
    }

    public Map<String, Object> getListReview(Integer page, Integer pageSize, Integer hotelId, String key) {
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.DESC, Constant.CREATED_DATE));
        List<Review> reviews = reviewRepository.filterReview(pageable, hotelId, Utils.checkNullString(key));
        Long count = reviewRepository.countReview(hotelId, Utils.checkNullString(key));

        Map<String, Object> stringObjectMap = Utils.putData(page, pageSize, count, reviews);
        return stringObjectMap;
    }
}
