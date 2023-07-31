package com.project.gream.domain.post.service;

import com.project.gream.common.annotation.LoginMember;
import com.project.gream.common.auth.dto.CustomUserDetails;
import com.project.gream.common.enumlist.PostType;
import com.project.gream.domain.member.dto.MemberDto;
import com.project.gream.domain.post.dto.*;
import com.project.gream.domain.post.entity.Likes;
import com.project.gream.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface PostService {

    PostResponseDto saveReview(ReviewDto reviewDto, List<String> imgPaths);
    Map<Integer, Integer> getReviewScoreByItemId(Long itemId);
    Page<ReviewDto> getReviewListByItemId(Long itemId, Pageable pageable);
    LikesResponseDto saveOrDeleteItemLike(LikesDto.Request request);
    LikesResponseDto checkLike(Long itemId, @LoginMember MemberDto memberDto);
    Page<PostDto> getAllNoticePosts(Pageable pageable);
    String saveNotice(PostRequestDto requestDto, List<MultipartFile> noticeImgs, MemberDto memberDto) throws Exception;
    PostResponseDto getNoticeDetail(Long noticeId);
    String deleteNotice(Long noticeId, CustomUserDetails user);
    PostResponseDto saveQna(PostRequestDto.QnaRequestDto postQnaDto,
                   List<MultipartFile> qnaImgs,
                   @LoginMember MemberDto memberDto) throws Exception;

    Page<Post> getQnaListByItemId(Long itemId, Pageable pageable);

    PostResponseDto getQnaDetail(Long qnaId);

    Page<Post> getQnaListByMemberId(String memberId, PostType postType, Pageable pageable);

    List<PostDto> getQnaListForMyPage(String memberId);

    Page<PostDto> searchNoticeByCondition(PostRequestDto requestDto, Pageable pageable);

    CommentDto.Response saveReviewComment(CommentDto.Request request);

    CommentDto.Response updateComment(CommentDto.Request request);

    @Transactional
    CommentDto.Response deleteComment(CommentDto.Request request);
}
