package com.project.gream.domain.post.service;

import com.project.gream.common.annotation.LoginMember;
import com.project.gream.common.auth.dto.CustomUserDetails;
import com.project.gream.domain.member.dto.MemberDto;
import com.project.gream.domain.post.dto.*;
import com.project.gream.domain.post.entity.Likes;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface PostService {

    void saveReview(ReviewDto reviewDto, List<String> imgPaths);
    Map<Integer, Integer> getReviewScoreByItemId(Long itemId);
    List<ReviewDto> getReviewListByItemId(Long itemId);
    Likes isAlreadyLiked(LikesVO likesVO);
    LikesResponseDto saveOrDeleteItemLike(LikesVO likesVO);
    LikesResponseDto checkLike(Long itemId, @LoginMember MemberDto memberDto);
    Page<PostDto> getAllNoticePosts(Pageable pageable);
    String saveNotice(PostRequestDto requestDto, List<MultipartFile> noticeImgs, MemberDto memberDto) throws Exception;
    PostResponseDto getNoticeDetail(Long noticeId);
    String deleteNotice(Long noticeId, CustomUserDetails user);
    String saveQna(PostRequestDto.QnaRequestDto postQnaDto,
                   List<MultipartFile> qnaImgs,
                   @LoginMember MemberDto memberDto) throws Exception;
}
