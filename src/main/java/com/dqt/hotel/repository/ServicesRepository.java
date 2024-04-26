package com.dqt.hotel.repository;

import com.dqt.hotel.dto.response.ServicesResponse;
import com.dqt.hotel.entity.Services;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ServicesRepository extends JpaRepository<Services, Integer> {
    @Modifying
    @Transactional
    @Query("update Services s set s.enabled = :enabled, s.modifiedBy = :modifiedBy, s.modifiedDate = CURRENT_TIMESTAMP where s.id = :id")
    void updateEnabled(@Param("enabled") Integer enabled, @Param("modifiedBy") String modifiedBy, @Param("id") Integer id);

    @Query("select s from Services s inner join Hotel h on h.id = s.hotelId where " +
            "(coalesce(:key, null) is null or s.serviceName like CONCAT('%', :key, '%') or s.description like CONCAT('%', :key, '%')) " +
            "and (:hotelId is null or s.hotelId = :hotelId) " +
            "and s.enabled = 1 " +
            "and h.enabled = 1")
    List<Services> filterAllService(@Param("key") String key, @Param("hotelId") Integer hotelId);

    @Query("select new com.dqt.hotel.dto.response.ServicesResponse(s.serviceName, s.hotelId, h.hotelName, s.createdDate, s.createdBy, s.modifiedDate, s.modifiedBy) from Services s inner join Hotel h on h.id = s.hotelId where " +
            "(coalesce(:key, null) is null or s.serviceName like CONCAT('%', :key, '%') or s.description like CONCAT('%', :key, '%')) " +
            "and (:hotelId is null or s.hotelId = :hotelId) " +
            "and s.enabled = 1 " +
            "and h.enabled = 1")
    List<ServicesResponse> filterAllServiceResponse(Pageable pageable, String key, Integer hotelId);

    @Query("select count(s) from Services s inner join Hotel h on h.id = s.hotelId where " +
            "(coalesce(:key, null) is null or s.serviceName like CONCAT('%', :key, '%') or s.description like CONCAT('%', :key, '%')) " +
            "and (:hotelId is null or s.hotelId = :hotelId) " +
            "and s.enabled = 1 " +
            "and h.enabled = 1")
    Long countFilterService(String key, Integer hotelId);
}
