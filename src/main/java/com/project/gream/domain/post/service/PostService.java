package com.project.gream.domain.post.service;

import com.project.gream.domain.item.dto.ItemDto;
import com.project.gream.domain.post.dto.LikesDto;
import com.project.gream.domain.post.dto.LikesResponseDto;
import com.project.gream.domain.post.dto.ReviewDto;
import com.project.gream.domain.post.entity.Likes;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface PostService {

    void saveReview(ReviewDto reviewDto, List<String> imgPaths);

    Map<Integer, Integer> getReviewScoreByItemId(Long itemId);
    List<ReviewDto> getReviewDtoById(Long itemId);
    String validateLike(LikesDto likesDto);

    Likes isAlreadyLiked(LikesDto likesDto);

    @Transactional
    LikesResponseDto saveOrDeleteLike(LikesDto likesDto);

}
