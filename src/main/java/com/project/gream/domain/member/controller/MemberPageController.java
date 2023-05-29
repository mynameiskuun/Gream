package com.project.gream.domain.member.controller;

import com.project.gream.common.annotation.LoginMember;
import com.project.gream.common.enumlist.Role;
import com.project.gream.domain.item.dto.ItemDto;
import com.project.gream.domain.item.service.ItemService;
import com.project.gream.domain.member.dto.CartItemDto;
import com.project.gream.domain.member.dto.MemberDto;
import com.project.gream.domain.member.entity.Cart;
import com.project.gream.domain.member.entity.CartItem;
import com.project.gream.domain.coupon.CouponBox;
import com.project.gream.domain.member.entity.Member;
import com.project.gream.domain.member.repository.CartItemRepository;
import com.project.gream.domain.member.repository.MemberRepository;
import com.project.gream.domain.order.dto.OrderItemDto;
import com.project.gream.domain.order.entity.OrderHistory;
import com.project.gream.domain.order.entity.OrderItem;
import com.project.gream.domain.order.repository.OrderHistoryRepository;
import com.project.gream.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
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
public class MemberPageController {

    private final ItemService itemService;
    private final OrderService orderService;

    @GetMapping("/mypage/{memberId}")
    public ModelAndView toBuyer(@PathVariable("memberId") String memberId) {
        List<OrderItemDto> orderItemList = orderService.findOrderItem(memberId);


        ModelAndView mav = new ModelAndView();
        mav.setViewName("member/mypage/customer/mypage-customer-main");
        mav.addObject("")
        return mav;
    }

    @GetMapping("/orderlist")
    public ModelAndView toMemberOrderList() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("member/mypage/customer/mypage-orderlist");
        return mav;

    }

    @GetMapping("/like")
    public ModelAndView toMemberLikeList() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("member/mypage/customer/mypage-like-list");
        return mav;

    }

    @GetMapping("/review")
    public ModelAndView toReviewWrite() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("member/mypage/customer/mypage-review-write");
        return mav;
    }

    @GetMapping("/postlist")
    public ModelAndView toPostList() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("member/mypage/customer/mypage-post-list");
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
        mav.setViewName("/member/login");
        mav.addObject("error", error);
        mav.addObject("exception", exception);
        return mav;
    }

    @GetMapping("/address")
    public ModelAndView toAdditionalPage() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("member/additional-input");
        return mav;
    }


//    @GetMapping("/admin/coupon")
//    public ModelAndView toCouponCreate() {
//        ModelAndView mav = new ModelAndView();
//        mav.setViewName("member/mypage/admin/create-coupon");
//        return mav;
//    }

//    @GetMapping("/admin/item/registration")
//    public ModelAndView toRegitem() {
//        ModelAndView mav = new ModelAndView();
//        mav.setViewName("/member/mypage/admin/admin-registration");
//        return mav;
//    }

//    @GetMapping("/admin")
//    public ModelAndView toAdmin() {
//        ModelAndView mav = new ModelAndView();
//        List<ItemDto> itemList = itemService.selectAllItems();
//        mav.addObject("itemList", itemList);
//        mav.setViewName("/member/mypage/admin/admin-main");
//        return mav;
//    }

    @GetMapping("/cart")
    public ModelAndView toCart(@LoginMember MemberDto memberDto) {
        ModelAndView mav = new ModelAndView();

        List<CartItemDto> cartItemList = null;
        if (memberDto == null) {
            mav.setViewName("member/login");
            return mav;
        } else {
            cartItemList = itemService.getCartItems(memberDto.getCartDto().getId());
        }
        mav.setViewName("/item/cart");
        mav.addObject("cartItem", cartItemList);
        // 장바구니에 담긴 상품 검색
        // List<Dto>로 mav에 저장 후 리턴
        return mav;
    }

}
