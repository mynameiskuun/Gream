package com.project.gream.domain.member.controller;

import com.project.gream.common.annotation.LoginMember;
import com.project.gream.domain.item.service.ItemService;
import com.project.gream.domain.member.dto.MemberVO;
import com.project.gream.domain.member.dto.MemberRequestDto;
import com.project.gream.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;
    private final ItemService itemService;
    private final HttpSession session;


    @PostMapping("/join")
    public int memberRegister(@RequestBody MemberVO memberVO) {
        return memberService.userRegister(memberVO);
    }


    // form 태그로 전해진 값은 JSON 형식이 아니기 때문에 @RequestBody를 붙이지 않는다.
    @PostMapping("/address")
    public ModelAndView updateAdditionalInformation(MemberRequestDto req, @LoginMember MemberVO memberVO) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("main/mainpage");
        memberService.updateAddressAndGender(req, memberVO);
        return mav;
    }
}
