package com.dqt.hotel.repository;

import com.dqt.hotel.dto.response.HotelResponse;
import com.dqt.hotel.entity.Hotel;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Integer> {

    Hotel findByIdAndEnabled(Integer id, Integer enable);

    @Modifying
    @Transactional
    @Query("update Hotel h set h.enabled = :enabled, h.modifiedBy = :modifiedBy, h.modifiedDate = CURRENT_TIMESTAMP where h.id = :id")
    void updateEnabled(@Param("enabled") Integer enabled, @Param("modifiedBy") String modifiedBy, @Param("id") Integer id);

    @Query("select h from Hotel h where (coalesce(:key, null) is null or h.hotelName like CONCAT('%', :key, '%') or h.address like CONCAT('%', :key, '%') or h.description like CONCAT('%', :key, '%')) and h.enabled = 1")
    List<Hotel> findAll(Pageable pageable, @Param("key") String key);

//    @Query("select new com.dqt.hotel.dto.response.HotelResponse(h.hotelName, h.address, h.description, h.createdDate, h.createdBy, h.modifiedDate, h.modifiedBy, null , null , null ) from Hotel h " +
//            "where (coalesce(:key, null) is null or h.hotelName like CONCAT('%', :key, '%') or h.address like CONCAT('%', :key, '%') or h.description like CONCAT('%', :key, '%')) and h.enabled = 1")
//    List<HotelResponse> filterHotel(Pageable pageable, @Param("key") String key);

    @Query("SELECT count(h) from Hotel h where (coalesce(:key, null) is null or h.hotelName like CONCAT('%', :key, '%') or h.address like CONCAT('%', :key, '%') or h.description like CONCAT('%', :key, '%')) and h.enabled = 1")
    Long countTotal(@Param("key") String key);


}
