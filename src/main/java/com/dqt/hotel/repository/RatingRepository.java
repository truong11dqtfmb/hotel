package com.dqt.hotel.repository;

import com.dqt.hotel.entity.Rating;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Integer> {

    List<Rating> findAllByHotelId(Integer hotelId);

    @Query("select r from Rating r where (:hotelId is null or r.hotelId = :hotelId) " +
            "and (:rate is null or r.rate = :rate)")
    List<Rating> filterRating(Pageable pageable, @Param("hotelId") Integer hotelId, @Param("rate") Integer rate);

    @Query("select count(r) from Rating r where (:hotelId is null or r.hotelId = :hotelId) " +
            "and (:rate is null or r.rate = :rate)")
    Long countRating(@Param("hotelId") Integer hotelId, @Param("rate") Integer rate);


    List<Rating> findByUserId(Integer userId);

}
