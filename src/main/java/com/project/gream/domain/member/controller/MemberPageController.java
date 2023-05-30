package com.project.gream.domain.member.controller;

import com.project.gream.common.annotation.LoginMember;
import com.project.gream.domain.item.service.ItemService;
import com.project.gream.domain.member.dto.CartItemDto;
import com.project.gream.domain.member.dto.MemberDto;
import com.project.gream.domain.order.dto.OrderItemDto;
import com.project.gream.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MemberPageController {

    private final ItemService itemService;
    private final OrderService orderService;

    @GetMapping("/mypage/{memberId}")
    public ModelAndView toBuyer(@PathVariable("memberId") String memberId) {
        List<OrderItemDto> orderItemList = orderService.findOrderItemForMypage(memberId);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("member/mypage/customer/mypage-customer-main");
        mav.addObject("orderItemList", orderItemList);

        return mav;
    }

    @GetMapping("/orderlist/{memberId}")
    public ModelAndView toMemberOrderList(@PathVariable("memberId") String memberId, @PageableDefault(size = 0, value = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<OrderItemDto> orderItemList = orderService.findAllOrderItem(memberId, pageable);
        ModelAndView mav = new ModelAndView();

        mav.addObject("orderItemList", orderItemList);
        mav.setViewName("member/mypage/customer/mypage-orderlist");
        return mav;
    }

    @GetMapping("/like/{memberId}")
    public ModelAndView toMemberLikeList(@PathVariable("memberId") String memberId) {
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

    @GetMapping("/test/{memberId}")
    public Page<OrderItemDto> test(@PathVariable("memberId") String memberId, @PageableDefault(size = 0, value = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<OrderItemDto> orderItemList = orderService.findAllOrderItem(memberId, pageable);
        ModelAndView mav = new ModelAndView();

        mav.addObject("orderItemList", orderItemList);
        mav.setViewName("member/mypage/customer/mypage-orderlist");
        return orderItemList;

    }

}
