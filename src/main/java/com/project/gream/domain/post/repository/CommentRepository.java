package com.project.gream.domain.post.repository;

import com.project.gream.domain.post.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "SELECT c " +
            "FROM Comment c " +
            "WHERE c.review.id = :reviewId " +
            "ORDER BY c.createdTime DESC")
    List<Comment> getReviewComments(Long reviewId);

    List<Comment> findAllByReview_Id(Long reviewId);
}
