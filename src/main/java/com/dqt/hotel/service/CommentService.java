package com.dqt.hotel.service;

import com.dqt.hotel.constant.Constant;
import com.dqt.hotel.dto.request.CommentRequest;
import com.dqt.hotel.dto.response.ResponseMessage;
import com.dqt.hotel.entity.Comment;
import com.dqt.hotel.entity.Hotel;
import com.dqt.hotel.entity.Review;
import com.dqt.hotel.repository.CommentRepository;
import com.dqt.hotel.repository.HotelRepository;
import com.dqt.hotel.repository.ReviewRepository;
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
public class CommentService {

    private final Utils utils;
    private final HotelRepository hotelRepository;
    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;

    public ResponseMessage commentHotel(CommentRequest request) {
        ResponseMessage responseMessage = validComment(request);
        if (!responseMessage.isStatus()) return responseMessage;

        Comment comment = Comment.builder()
                .commentId(request.getCommentId())
                .content(request.getContent())
                .type(request.getType())
                .createdBy(utils.gerCurrentUser().getId().toString())
                .createdDate(new Date())
                .build();

        Comment save = commentRepository.save(comment);
        return ResponseMessage.ok("Thank you comment", save);
    }

    private ResponseMessage validComment(CommentRequest request) {
        if (Objects.isNull(request.getCommentId())) {
            return ResponseMessage.error("Id hotel or review not null");
        }
        if (request.getContent().isBlank()) {
            return ResponseMessage.error("Content is blank");
        }
        if (request.getType() != Constant.TYPE_HOTEL && request.getType() != Constant.TYPE_PREVIEW) {
            return ResponseMessage.error("Type is 1: hotel || 2: review");
        }
        if (request.getType() == Constant.TYPE_HOTEL) {
            Optional<Hotel> optionalHotel = hotelRepository.findById(request.getCommentId());
            if (!optionalHotel.isPresent()) {
                return ResponseMessage.error("Hotel not found");
            }
        } else if (request.getType() == Constant.TYPE_HOTEL) {
            Optional<Review> optionalReview = reviewRepository.findById(request.getCommentId());
            if (!optionalReview.isPresent()) {
                return ResponseMessage.error("Review not found");
            }
        }
        return ResponseMessage.ok("Validate successfully");
    }

    public ResponseMessage deleteComment(Integer id) {
        Optional<Comment> optional = commentRepository.findById(id);
        if (!optional.isPresent()) return ResponseMessage.error("Could not found by id: " + id);
        commentRepository.deleteById(id);
        return ResponseMessage.ok("Comment deleted: " + id);
    }

    public Map<String, Object> getListComment(Integer page, Integer pageSize,Integer type, Integer id, String key) {
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.DESC, Constant.CREATED_DATE));
        List<Comment> comments = commentRepository.filterComment(pageable, type, id, Utils.checkNullString(key));
        Long count = commentRepository.countComment(type, id, Utils.checkNullString(key));

        Map<String, Object> stringObjectMap = Utils.putData(page, pageSize, count, comments);
        return stringObjectMap;
    }
}
