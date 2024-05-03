package com.dqt.hotel.repository;

import com.dqt.hotel.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    @Query("select r from Review r where (coalesce(:key, null) is null or r.title like CONCAT('%', :key, '%')) " +
            "and (:hotelId is null or r.hotelId = :hotelId) ")
    List<Review> filterReview(Pageable pageable, @Param("hotelId") Integer hotelId, @Param("key") String key);

    @Query("select count(r) from Review r where (coalesce(:key, null) is null or r.title like CONCAT('%', :key, '%')) " +
            "and (:hotelId is null or r.hotelId = :hotelId) ")
    Long countReview(@Param("hotelId") Integer hotelId, @Param("key") String key);
}
