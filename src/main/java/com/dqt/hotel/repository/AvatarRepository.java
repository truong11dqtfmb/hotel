package com.dqt.hotel.repository;

import com.dqt.hotel.entity.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AvatarRepository extends JpaRepository<Avatar, Integer> {

    @Query("select a.imageName from Avatar a where a.imageId = :id and a.type = :type order by a.createdDate desc ")
    List<String> findAvatarById(@Param("id") Integer id, @Param("type") Integer type);

    @Query("select a from Avatar a where (coalesce(:ids, null) is NULL OR a.imageId IN (:ids)) and a.type = :type order by a.createdDate desc ")
    List<Avatar> findAvatarByIds(@Param("ids") List<Integer> ids, @Param("type") Integer type);

}