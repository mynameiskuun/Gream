package com.project.gream.domain.member.controller;

import com.project.gream.common.annotation.LoginMember;
import com.project.gream.common.enumlist.Role;
import com.project.gream.domain.member.dto.CartItemDto;
import com.project.gream.domain.member.dto.MemberDto;
import com.project.gream.domain.member.dto.MemberRequestDto;
import com.project.gream.domain.member.entity.Cart;
import com.project.gream.domain.member.entity.Member;
import com.project.gream.domain.member.repository.MemberRepository;
import com.project.gream.domain.member.service.MemberService;
import com.project.gream.domain.order.dto.OrderRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;
    private final HttpSession session;
    private final PasswordEncoder encoder;
    private final MemberRepository memberRepository;

    @GetMapping("/")
    public ModelAndView toMainPage(@AuthenticationPrincipal Object principal, @LoginMember MemberDto memberDto) {

        if (memberRepository.findById("admin").isEmpty()) {
            Member admin = Member.builder()
                    .id("admin")
                    .password(encoder.encode("1111"))
                    .role(Role.ADMIN)
                    .cart(new Cart())
//                    .couponBox(new CouponBox())
                    .build();
            memberRepository.save(admin);
        }

        Object context = session.getAttribute("SPRING_SECURITY_CONTEXT");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("memberVO : " + memberDto);
        log.info("security Context : " + context);
        log.info("principal" + principal);
        log.info("authentication" + authentication);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("main/mainpage");
        return mav;
    }
    @PostMapping("/join")
    public int memberRegister(@RequestBody MemberDto memberDto) {
        return memberService.userRegister(memberDto);
    }


    // form 태그로 전해진 값은 JSON 형식이 아니기 때문에 @RequestBody를 붙이지 않는다.
    @PostMapping("/address")
    public ModelAndView updateAdditionalInformation(MemberRequestDto req, @LoginMember MemberDto memberDto) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("main/mainpage");
        memberService.updateAddressAndGender(req, memberDto);
        return mav;
    }

}
