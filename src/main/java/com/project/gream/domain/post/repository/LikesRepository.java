package com.project.gream.domain.post.repository;

import com.project.gream.common.enumlist.LikeTargetType;
import com.project.gream.domain.post.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    boolean existsByItem_IdAndMember_Id(Long itemId, String memberId);
    boolean existsByReview_IdAndMember_Id(Long reviewId, String memberId);
    Long countByItem_Id(long itemId);
    Long countByReview_Id(long reviewId);
    Long countByComment_Id(long commentId);
    Long countByPost_Id(long postId);
}
