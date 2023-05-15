package com.project.gream.domain.member.controller;

import com.project.gream.common.annotation.LoginMember;
import com.project.gream.domain.member.dto.MemberDto;
import com.project.gream.domain.member.dto.MemberRequestDto;
import com.project.gream.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;
    private final HttpSession session;

    @GetMapping("/")
    public ModelAndView toMainPage(@AuthenticationPrincipal Object principal, @LoginMember MemberDto memberDto) {

        Object context = session.getAttribute("SPRING_SECURITY_CONTEXT");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("memberDto : " + memberDto);
        log.info("security Context : " + context);
        log.info("principal" + principal);
        log.info("authentication" + authentication);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("main/mainpage");
        return mav;
    }

    @GetMapping("/join")
    public ModelAndView toJoinPage() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("member/join");
        return mav;
    }

    @PostMapping("/join")
    public int memberRegister(@RequestBody MemberDto memberDto) {
        return memberService.userRegister(memberDto);
    }

    @GetMapping("/login")
    public ModelAndView toLoginPage(@RequestParam(value = "error", required = false) String error,
                                    @RequestParam(value = "exception", required = false) String exception) {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("member/login");
        mav.addObject("error", error);
        mav.addObject("exception", exception);
        return mav;
    }

    @GetMapping("/address")
    public ModelAndView toAdditionlPage() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("member/additional-input");
        return mav;
    }

    // form 태그로 전해진 값은 JSON 형식이 아니기 때문에 @RequestBody를 붙이지 않는다.
    @PostMapping("/address")
    public void updateAdditionalInformation(MemberRequestDto req, @LoginMember MemberDto memberDto) {
        memberService.updateAddressAndGender(req, memberDto);
    }
}
