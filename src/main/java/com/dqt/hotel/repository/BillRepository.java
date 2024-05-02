package com.dqt.hotel.repository;

import com.dqt.hotel.dto.response.BillResponse;
import com.dqt.hotel.entity.Bill;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface BillRepository extends JpaRepository<Bill, Integer> {
    Bill findBillByBookingId(Integer booking);

    @Query("select new com.dqt.hotel.dto.response.BillResponse(bi.id, b.hotelId, h.hotelName, b.roomId, r.roomName, b.userId, u.fullName, u.email, u.phone, u.identity, bi.bookingId, bi.amount, b.checkInDate, b.checkOutDate,bi.paymentDate, b.createdDate, b.createdBy, b.modifiedDate, b.modifiedBy) from Bill bi " +
            "join Booking b on b.id = bi.bookingId " +
            "join Hotel h on b.hotelId = h.id " +
            "join User u on b.userId = u.id " +
            "join Room r on b.roomId = r.id " +
            "where (:hotelId is null or b.hotelId = :hotelId) " +
            "and (:userId is null or b.userId = :userId) " +
            "and (:amount is null or bi.amount >= :amount) " +
            "and (coalesce(:startDate, null)  is null or bi.paymentDate >= :startDate) " +
            "and (coalesce(:endDate, null)  is null or bi.paymentDate <= :endDate) " +
            "and (:roomId is null or b.roomId = :roomId) ")
    List<BillResponse> filterBill(Pageable pageable, @Param("hotelId") Integer hotelId, @Param("roomId") Integer roomId, @Param("userId") Integer userId, @Param("amount") Integer amount, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("select count(bi) from Bill bi " +
            "join Booking b on b.id = bi.bookingId " +
            "join Hotel h on b.hotelId = h.id " +
            "join User u on b.userId = u.id " +
            "join Room r on b.roomId = r.id " +
            "where (:hotelId is null or b.hotelId = :hotelId) " +
            "and (:userId is null or b.userId = :userId) " +
            "and (:amount is null or bi.amount >= :amount) " +
            "and (coalesce(:startDate, null)  is null or bi.paymentDate >= :startDate) " +
            "and (coalesce(:endDate, null)  is null or bi.paymentDate <= :endDate) " +
            "and (:roomId is null or b.roomId = :roomId) ")
    Long countBill(@Param("hotelId") Integer hotelId, @Param("roomId") Integer roomId, @Param("userId") Integer userId, @Param("amount") Integer amount, @Param("startDate") Date startDate, @Param("endDate") Date endDate);


    @Query("select b from Bill b where (coalesce(:date, null) is null or b.paymentDate <= :date)")
    List<Bill> filterBillPayment(Date date);
}
