package com.dqt.hotel.repository;

import com.dqt.hotel.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query("select c from Comment c where (coalesce(:key, null) is null or c.content like CONCAT('%', :key, '%'))" +
            "and (:type is null or c.type = :type)" +
            "and (:id is null or c.commentId = :id)")
    List<Comment> filterComment(Pageable pageable, @Param("type") Integer type, @Param("id") Integer id, @Param("key") String key);

    @Query("select count(c) from Comment c where (coalesce(:key, null) is null or c.content like CONCAT('%', :key, '%'))" +
            "and (:type is null or c.type = :type)" +
            "and (:id is null or c.commentId = :id)")
    Long countComment( @Param("type") Integer type, @Param("id") Integer id, @Param("key") String key);
}
