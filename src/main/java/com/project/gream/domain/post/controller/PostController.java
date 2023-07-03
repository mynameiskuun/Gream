package com.project.gream.domain.post.controller;

import com.project.gream.common.config.S3Config;
import com.project.gream.domain.post.dto.*;
import com.project.gream.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

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

    @GetMapping("/post/notice")
    public ModelAndView toQnaBoard(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        ModelAndView mav = new ModelAndView();
        Page<PostDto> postsList = postService.getAllQnaPosts(pageable);

        int nowPage = postsList.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 9, postsList.getTotalPages());

        mav.addObject("nowPage", nowPage);
        mav.addObject("startPage", startPage);
        mav.addObject("endPage", endPage);
        mav.addObject("postslist", postsList);
        mav.setViewName("post/post-notice");
        return mav;
    }

    @PostMapping("/post/notice/search")
    public String searchPosts(@RequestBody PostRequestDto postRequestDto) {

        log.info("-------------------- 게시글 검색 start");
        log.info("period : " + postRequestDto.getPeriod());
        log.info("target : " + postRequestDto.getTarget());
        log.info("keywords : " + postRequestDto.getSearchKeyWords());

        if (postRequestDto.getPeriod() == null || postRequestDto.getTarget() == null) {

            return "둘 중 하나 null";
        }
        return "성공";
    }
}
