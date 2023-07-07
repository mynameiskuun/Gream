package com.project.gream.domain.post.controller;

import com.project.gream.common.annotation.LoginMember;
import com.project.gream.common.auth.dto.CustomUserDetails;
import com.project.gream.common.config.S3Config;
import com.project.gream.common.enumlist.Role;
import com.project.gream.domain.item.dto.ItemDto;
import com.project.gream.domain.item.service.ItemService;
import com.project.gream.domain.member.dto.MemberDto;
import com.project.gream.domain.post.dto.*;
import com.project.gream.domain.post.entity.Post;
import com.project.gream.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final ItemService itemService;
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
    public ModelAndView toNoticeBoard(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        ModelAndView mav = new ModelAndView();
        Page<PostDto> noticeList = postService.getAllNoticePosts(pageable);

        int nowPage = noticeList.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 9, noticeList.getTotalPages());

        log.info("nowPage : " + nowPage);
        log.info("startPage : " + startPage);
        log.info("endPage : " + endPage);
        log.info("postsList.getTotalPages() : " + noticeList.getTotalPages());

        mav.addObject("nowPage", nowPage);
        mav.addObject("startPage", startPage);
        mav.addObject("endPage", endPage);
        mav.addObject("noticeList", noticeList);

        mav.setViewName("post/post-notice");
        return mav;
    }

    @PostMapping("/post/notice/search")
    public String searchPosts(@RequestBody PostRequestDto postRequestDto) {

        log.info("-------------------- 게시글 검색 start");
        log.info("period : " + postRequestDto.getSearchPeriod());
        log.info("target : " + postRequestDto.getSearchTarget());
        log.info("keywords : " + postRequestDto.getSearchKeyWords());

        if (postRequestDto.getSearchPeriod() == null || postRequestDto.getSearchTarget() == null) {

            return "둘 중 하나 null";
        }
        return "성공";
    }

    @GetMapping("/post/notice/write")
    public ModelAndView toNoticeWrite() {
        ModelAndView mav = new ModelAndView();

        mav.setViewName("post/post-notice-write");
        return mav;
    }

    @PostMapping("/post/notice")
    public String saveNotice(@RequestPart("postRequestDto") PostRequestDto requestDto,
                             @RequestPart("noticeImgs") List<MultipartFile> noticeImgs,
                             @LoginMember MemberDto memberDto) throws Exception {

        log.info("------------------ notice save start");
        log.info(String.valueOf(requestDto));
        log.info(String.valueOf(noticeImgs.get(0)));

        return postService.saveNotice(requestDto, noticeImgs, memberDto);
    }

    @GetMapping("/post/notice/{noticeId}")
    public ModelAndView getNoticeDetail(@PathVariable Long noticeId) {
        ModelAndView mav = new ModelAndView();
        PostResponseDto response = postService.getNoticeDetail(noticeId);

        mav.addObject("notice", response);
        mav.setViewName("post/post-notice-detail");
        return mav;
    }

    @GetMapping("/post/notice/{noticeId}/edit")
    public ModelAndView toNoticeModify(@PathVariable Long noticeId, @LoginMember MemberDto memberDto) {
        ModelAndView mav = new ModelAndView();
        if (memberDto.getRole() != Role.ADMIN) {
            mav.setViewName("/main/mainpage");
            return mav;
        }
        PostResponseDto response = postService.getNoticeDetail(noticeId);

        mav.addObject("postResponseDto", response);
        mav.setViewName("post/post-notice-write");
        return mav;
    }

    @DeleteMapping("/post/notice/{noticeId}")
    public String deleteNotice(@PathVariable Long noticeId, @AuthenticationPrincipal CustomUserDetails user) {

        return postService.deleteNotice(noticeId, user);
    }

    @GetMapping("/post/qna/{itemId}")
    public ModelAndView toQnaWrite(@PathVariable Long itemId) {
        ModelAndView mav = new ModelAndView();
        ItemDto itemDto = itemService.getItemById(itemId);

        mav.addObject("itemDto", itemDto);
        mav.setViewName("post/qna-write-popup");
        return mav;
    }

    @PostMapping("/post/qna")
    public String saveQna(@RequestPart("qnaRequestDto") PostRequestDto.QnaRequestDto postQnaDto,
                          @RequestPart("qnaImgs") List<MultipartFile> qnaImgs,
                          @LoginMember MemberDto memberDto) throws Exception {

        log.info("------------------------ saveQna request start");

        return postService.saveQna(postQnaDto, qnaImgs, memberDto);
    }
}
