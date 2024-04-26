package com.dqt.hotel.repository;

import com.dqt.hotel.dto.response.RoomResponse;
import com.dqt.hotel.entity.Room;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Integer> {

    Room findByIdAndEnabled(Integer id, Integer enable);

    @Modifying
    @Transactional
    @Query("update Room r set r.enabled = :enabled, r.modifiedBy = :modifiedBy, r.modifiedDate = CURRENT_TIMESTAMP where r.id = :id")
    void updateEnabled(@Param("enabled") Integer enabled, @Param("modifiedBy") String modifiedBy, @Param("id") Integer id);


    @Query("select r from Room r inner join Hotel h on h.id = r.hotelId where " +
            "(coalesce(:key, null) is null or r.description like CONCAT('%', :key, '%')) " +
            "and (:hotelId is null or r.hotelId = :hotelId) " +
            "and (:acreage is null or r.acreage >= :acreage) " +
            "and (:member is null or r.member >= :member) " +
            "and (:priceMax is null or r.price <= :priceMax) " +
            "and (:priceMin is null or r.price >= :priceMin) " +
            "and r.enabled = 1 " +
            "and h.enabled = 1")
    List<Room> findAllRoom(@Param("key") String key, @Param("hotelId") Integer hotelId, @Param("acreage") Integer acreage, @Param("member") Integer member, @Param("priceMax") Integer priceMax, @Param("priceMin") Integer priceMin);

    @Query("select new com.dqt.hotel.dto.response.RoomResponse(r.roomName, r.hotelId, h.hotelName, r.description, r.acreage, r.member, r.price, r.createdDate, r.createdBy, r.modifiedDate, r.modifiedBy) from Room r inner join Hotel h on h.id = r.hotelId where " +
            "(coalesce(:key, null) is null or r.description like CONCAT('%', :key, '%')) " +
            "and (:hotelId is null or r.hotelId = :hotelId) " +
            "and (:acreage is null or r.acreage >= :acreage) " +
            "and (:member is null or r.member >= :member) " +
            "and (:priceMax is null or r.price <= :priceMax) " +
            "and (:priceMin is null or r.price >= :priceMin) " +
            "and r.enabled = 1 " +
            "and h.enabled = 1")
    List<RoomResponse> filterAllRoomResponse(Pageable pageable, @Param("key") String key, @Param("hotelId") Integer hotelId, @Param("acreage") Integer acreage, @Param("member") Integer member, @Param("priceMax") Integer priceMax, @Param("priceMin") Integer priceMin);


    @Query("select count(r) from Room r inner join Hotel h on h.id = r.hotelId where " +
            "(coalesce(:key, null) is null or r.description like CONCAT('%', :key, '%')) " +
            "and (:hotelId is null or r.hotelId = :hotelId) " +
            "and (:acreage is null or r.acreage >= :acreage) " +
            "and (:member is null or r.member >= :member) " +
            "and (:priceMax is null or r.price <= :priceMax) " +
            "and (:priceMin is null or r.price >= :priceMin) " +
            "and r.enabled = 1 " +
            "and h.enabled = 1")
    Long countFilterRoom(@Param("key") String key, @Param("hotelId") Integer hotelId, @Param("acreage") Integer acreage, @Param("member") Integer member, @Param("priceMax") Integer priceMax, @Param("priceMin") Integer priceMin);


}
