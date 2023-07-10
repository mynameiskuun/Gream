package com.project.gream.domain.member.controller;

import com.project.gream.common.annotation.LoginMember;
import com.project.gream.domain.item.dto.CouponDto;
import com.project.gream.domain.item.dto.ItemDto;
import com.project.gream.domain.item.dto.UserCouponResponseDto;
import com.project.gream.domain.item.service.ItemService;
import com.project.gream.domain.member.dto.CartItemDto;
import com.project.gream.domain.member.dto.MemberDto;
import com.project.gream.domain.member.entity.Member;
import com.project.gream.domain.order.dto.OrderItemDto;
import com.project.gream.domain.order.entity.OrderItem;
import com.project.gream.domain.order.repository.OrderItemRepository;
import com.project.gream.domain.order.service.OrderService;
import com.project.gream.domain.post.dto.PostDto;
import com.project.gream.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MemberPageController {

    private final ItemService itemService;
    private final OrderService orderService;
    private final PostService postService;
    private final OrderItemRepository orderItemRepository;

    @GetMapping("/mypage/{memberId}")
    public ModelAndView toBuyer(@PathVariable("memberId") String memberId) {
        List<OrderItemDto> userOrderItemList = orderService.findOrderItemForMypage(memberId);
        List<ItemDto> userLikeItems = itemService.getLikedItemListForMypage(itemService.getLikedItemIds(memberId));
        List<UserCouponResponseDto> userCouponList = itemService.getMemberCouponForMypage(memberId);
        List<PostDto> userQnaList = postService.getQnaListForMyPage(memberId);

        ModelAndView mav = new ModelAndView();
        mav.addObject("qnaList", userQnaList);
        mav.addObject("couponList", userCouponList);
        mav.addObject("likeItems", userLikeItems);
        mav.addObject("orderItemList", userOrderItemList);
        mav.setViewName("member/mypage/customer/mypage-customer-main");

        return mav;
    }

    @GetMapping("/orderlist/{memberId}")
    public ModelAndView toMemberOrderList(@PathVariable("memberId") String memberId, @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<OrderItemDto> orderItemList = orderService.findAllOrderItem(memberId, pageable);

        ModelAndView mav = new ModelAndView();
        int nowPage = orderItemList.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 9, orderItemList.getTotalPages());

        mav.addObject("nowPage", nowPage);
        mav.addObject("startPage", startPage);
        mav.addObject("endPage", endPage);
        mav.addObject("orderItemList", orderItemList);
        mav.setViewName("member/mypage/customer/mypage-orderlist");
        return mav;
    }

    @GetMapping("/like/{memberId}")
    public ModelAndView toMemberLikeList(@PathVariable("memberId") String memberId) {
        ModelAndView mav = new ModelAndView();

        List<ItemDto> likedItemList = itemService.getLikedItemListByMemberId(memberId);
        mav.addObject("likedItemList", likedItemList);
        mav.setViewName("member/mypage/customer/mypage-like-list");
        return mav;

    }

    @GetMapping("/review/{orderItemId}")
    public ModelAndView toReviewWrite(@PathVariable("orderItemId") Long orderItemId) {

        log.info("-------------------------------- 배송 완료된 상품 리뷰 작성");

        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new NoSuchElementException(String.format("상품번호 [%s]번은 존재하지 않는 상품입니다.", orderItemId)));

        ModelAndView mav = new ModelAndView();
        mav.addObject("orderItemDetail", OrderItemDto.fromEntity(orderItem));
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
        mav.setViewName("member/login");
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
//        mav.setViewName("member/mypage/admin/admin-registration");
//        return mav;
//    }

//    @GetMapping("/admin")
//    public ModelAndView toAdmin() {
//        ModelAndView mav = new ModelAndView();
//        List<ItemDto> itemList = itemService.selectAllItems();
//        mav.addObject("itemList", itemList);
//        mav.setViewName("member/mypage/admin/admin-main");
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
        mav.setViewName("item/cart");
        mav.addObject("cartItem", cartItemList);
        // 장바구니에 담긴 상품 검색
        // List<Dto>로 mav에 저장 후 리턴
        return mav;
    }

    @GetMapping("/test/{memberId}")
    public Page<OrderItemDto> test(@PathVariable("memberId") String memberId, @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<OrderItemDto> orderItemList = orderService.findAllOrderItem(memberId, pageable);
        ModelAndView mav = new ModelAndView();

        mav.addObject("orderItemList", orderItemList);
        mav.setViewName("member/mypage/customer/mypage-orderlist");
        return orderItemList;

    }

    @GetMapping("/member/coupon")
    public ModelAndView getMemberCoupon(@LoginMember MemberDto memberDto) {

        log.info("------------------------------ 유저가 가지고 있는 쿠폰 조회");
        ModelAndView mav = new ModelAndView();
        List<UserCouponResponseDto> userCouponList = itemService.getMemberCoupon(memberDto.toEntity());

        log.info("userCouponList : " + userCouponList);
        mav.addObject("userCouponList", userCouponList);
        mav.setViewName("/member/mypage/customer/mypage-customer-coupon");
        return mav;
    }
}
