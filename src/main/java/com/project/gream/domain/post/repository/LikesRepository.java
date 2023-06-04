package com.project.gream.domain.post.repository;

import com.project.gream.common.enumlist.LikeTargetType;
import com.project.gream.domain.post.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    boolean existsByTargetIdAndLikeTargetType(Long targetId, LikeTargetType likeTargetType);
    Optional<Likes> getByTargetIdAndLikeTargetType(Long targetId, LikeTargetType likeTargetType);

}
