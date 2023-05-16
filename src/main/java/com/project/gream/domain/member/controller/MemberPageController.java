package com.project.gream.domain.member.controller;

import com.project.gream.common.annotation.LoginMember;
import com.project.gream.common.enumlist.Role;
import com.project.gream.domain.item.dto.ItemVO;
import com.project.gream.domain.item.service.ItemService;
import com.project.gream.domain.member.dto.MemberVO;
import com.project.gream.domain.member.entity.Cart;
import com.project.gream.domain.member.entity.Member;
import com.project.gream.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MemberPageController {

    private final HttpSession session;
    private final ItemService itemService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;

    @GetMapping("/")
    public ModelAndView toMainPage(@AuthenticationPrincipal Object principal, @LoginMember MemberVO memberVO) {

        if (memberRepository.findById("admin").isEmpty()) {
            Member admin = Member.builder()
                            .id("admin")
                            .password(encoder.encode("1111"))
                            .role(Role.ADMIN)
                            .cart(new Cart())
                            .build();
            memberRepository.save(admin);
        }

        Object context = session.getAttribute("SPRING_SECURITY_CONTEXT");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("memberVO : " + memberVO);
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
    @GetMapping("/user/order")
    public ModelAndView toOrderList() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("mypage/customer/mypage-orderlist");
        return mav;
    }
    @GetMapping("/user/review")
    public ModelAndView toReviewWrite() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("board/review/mypage-customer-review-write");
        return mav;
    }
    @GetMapping("/user/postlist")
    public ModelAndView toPostList() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("mypage/customer/mypage-post-list");
        return mav;
    }
    @GetMapping("/user/orderlist")
    public ModelAndView toUserOrderList() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("mypage/customer/mypage-orderlist");
        return mav;
    }
    @GetMapping("/user/mypage/{memberId}")
    public ModelAndView toBuyer(@PathVariable("memberId") String memberId) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/mypage/customer/mypage-customer-main");
        return mav;
    }
    @GetMapping("/admin/coupon")
    public ModelAndView toCouponCreate() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("mypage/admin/create-coupon");
        return mav;
    }
    @GetMapping("/admin/item/registration")
    public ModelAndView toRegitem() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/mypage/admin/admin-registration");
        return mav;
    }
    @GetMapping("/user/like")
    public ModelAndView toLikeList() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("mypage/customer/mypage-like-list");
        return mav;
    }
    @GetMapping("/admin")
    public ModelAndView toAdmin() {
        ModelAndView mav = new ModelAndView();
        List<ItemVO> itemList = itemService.selectAllItems();
        mav.addObject("itemList", itemList);
        mav.setViewName("/mypage/admin/admin-main");
        return mav;
    }
//    @GetMapping("/user/cart")
//    public ModelAndView toCart(@LoginMember memberVO userDto) {
//
//        ModelAndView mav = new ModelAndView();
//        if(userDto == null) {
//            mav.setViewName("member/login");
//        }
//        else {
//            Optional<List<ImgDto>> imgList = Optional.ofNullable(imgService.selectImgsDistinct());
//            Optional<List<CartItemDto>> cartItemList = Optional.ofNullable(userService.getCartItemsByUserId(userDto));
//
//            mav.addObject("imgList", imgList.get());
//            mav.addObject("cartItemList", cartItemList.get());
//            mav.setViewName("/cart/cart");
//        }
//        return mav;
//    }
}
