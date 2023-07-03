package com.project.gream.domain.post.service;

import com.project.gream.common.annotation.LoginMember;
import com.project.gream.domain.member.dto.MemberDto;
import com.project.gream.domain.post.dto.*;
import com.project.gream.domain.post.entity.Likes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface PostService {

    void saveReview(ReviewDto reviewDto, List<String> imgPaths);
    Map<Integer, Integer> getReviewScoreByItemId(Long itemId);
    List<ReviewDto> getReviewDtoById(Long itemId);
    Likes isAlreadyLiked(LikesVO likesVO);
    LikesResponseDto saveOrDeleteItemLike(LikesVO likesVO);
    LikesResponseDto checkLike(Long itemId, @LoginMember MemberDto memberDto);
    Page<PostDto> getAllQnaPosts(Pageable pageable);
//    LikesResponseDto checkLike(LikesRequestDto req, @LoginMember MemberDto memberDto, );
}
