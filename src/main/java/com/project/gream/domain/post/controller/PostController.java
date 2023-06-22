package com.project.gream.domain.post.controller;

import com.project.gream.common.annotation.LoginMember;
import com.project.gream.common.config.S3Config;
import com.project.gream.domain.member.dto.MemberDto;
import com.project.gream.domain.post.dto.LikesRequestDto;
import com.project.gream.domain.post.dto.LikesVO;
import com.project.gream.domain.post.dto.LikesResponseDto;
import com.project.gream.domain.post.dto.ReviewDto;
import com.project.gream.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;
    private final S3Config s3Config;

    @PostMapping("/review")
    public String saveReview(@RequestPart("reviewDto") ReviewDto reviewDto,
                             @RequestPart("imgFiles") List<MultipartFile> imgFiles) throws Exception {

        log.info("------------------------ review data 전송 완료.");

        List<String> imgPaths = s3Config.imgUpload(reviewDto.getClass(), imgFiles);
        postService.saveReview(reviewDto, imgPaths);

        return "";
    }

    @PostMapping("/item/like")
    public LikesResponseDto itemLike(@RequestBody LikesVO likesVO) {
        return postService.saveOrDeleteItemLike(likesVO);
    }

    @PostMapping("/test/likecheck")
    public String likeCheck(@RequestBody LikesRequestDto req, @LoginMember MemberDto memberDto) {

//        postService.checkLike(req, memberDto);
        return "";
    }
}
