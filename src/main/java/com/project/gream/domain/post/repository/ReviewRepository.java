package com.project.gream.domain.post.repository;

import com.project.gream.domain.post.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r.starValue " +
            "FROM Review r " +
            "WHERE r.item.id = :itemId")
    List<Integer> getReviewScore(@Param("itemId") Long itemId);
    List<Review> findAllById(Long itemId);
    Page<Review> findAllByItem_Id(Long itemId, Pageable pageable);
    List<Review> findAllByItem_Id(Long itemId);
}
