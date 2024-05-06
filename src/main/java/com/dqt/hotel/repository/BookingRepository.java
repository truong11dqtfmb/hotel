package com.dqt.hotel.repository;

import com.dqt.hotel.dto.response.BookingResponse;
import com.dqt.hotel.entity.Booking;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @Query("SELECT b from Booking b where b.checkOutDate < CURRENT_TIMESTAMP order by b.checkOutDate desc")
    List<Booking> checkRoom();

    List<Booking> findAllByUserId(Integer userId);

    @Query("select b from Booking b " +
            "join Hotel h on b.hotelId = h.id " +
            "join User u on b.userId = u.id " +
            "join Room r on b.roomId = r.id " +
            "where (:hotelId is null or b.hotelId = :hotelId) " +
            "and (:userId is null or b.userId = :userId) " +
            "and (:roomId is null or b.roomId = :roomId) ")
    List<Booking> findAllBooking(Pageable pageable, @Param("hotelId") Integer hotelId, @Param("roomId") Integer roomId, @Param("userId") Integer userId);

    @Query("select new com.dqt.hotel.dto.response.BookingResponse(b.id, b.description, b.hotelId, h.hotelName, b.roomId, r.roomName, b.userId, u.fullName, u.email, u.phone, u.identity, b.checkInDate, b.checkOutDate, b.createdDate, b.createdBy, b.modifiedDate, b.modifiedBy) from Booking b " +
            "join Hotel h on b.hotelId = h.id " +
            "join User u on b.userId = u.id " +
            "join Room r on b.roomId = r.id " +
            "where (:hotelId is null or b.hotelId = :hotelId) " +
            "and (:userId is null or b.userId = :userId) " +
            "and (:roomId is null or b.roomId = :roomId) " +
            "and (coalesce(:startDate, null)  is null or b.checkInDate >= :startDate) " +
            "and (coalesce(:endDate, null)  is null or b.checkOutDate <= :endDate) ")
    List<BookingResponse> filterBooking(Pageable pageable, @Param("hotelId") Integer hotelId, @Param("roomId") Integer roomId, @Param("userId") Integer userId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("select count(b) from Booking b " +
            "join Hotel h on b.hotelId = h.id " +
            "join User u on b.userId = u.id " +
            "join Room r on b.roomId = r.id " +
            "where (:hotelId is null or b.hotelId = :hotelId) " +
            "and (:userId is null or b.userId = :userId) " +
            "and (:roomId is null or b.roomId = :roomId) " +
            "and (coalesce(:startDate, null)  is null or b.checkInDate >= :startDate) " +
            "and (coalesce(:endDate, null)  is null or b.checkOutDate <= :endDate) ")
    Long countBooking(@Param("hotelId") Integer hotelId, @Param("roomId") Integer roomId, @Param("userId") Integer userId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("select b from Booking b where b.hotelId = :hotelId and  b.userId = :userId")
    List<Booking> checkRating(@Param("hotelId") Integer hotelId, @Param("userId") Integer userId);

    @Query("select b from Booking b where (coalesce(:startDate, null)  is null or b.checkInDate >= :startDate) " +
            "and (coalesce(:endDate, null)  is null or b.checkOutDate <= :endDate) ")
    List<Booking> findAllBetweenTime(@Param("startDate") java.util.Date startDate, @Param("endDate") java.util.Date endDate);

}
