package com.project.gream.domain.item.controller;

import com.project.gream.common.annotation.LoginMember;
import com.project.gream.domain.item.dto.*;
import com.project.gream.domain.item.service.ItemService;
import com.project.gream.domain.member.dto.CartItemDto;
import com.project.gream.domain.member.dto.MemberDto;
import com.project.gream.domain.member.repository.CartItemRepository;
import com.project.gream.domain.order.dto.OrderItemDto;
import com.project.gream.domain.order.service.OrderService;
import com.project.gream.domain.post.dto.LikesResponseDto;
import com.project.gream.domain.post.dto.ReviewDto;
import com.project.gream.domain.post.repository.ReviewRepository;
import com.project.gream.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ItemController {

    private final ItemService itemService;
    private final PostService postService;
    private final OrderService orderService;

    @PostMapping("/cart")
    public String addItemToCart(@RequestBody CartItemDto cartItemDto, @LoginMember MemberDto memberDto) {
        log.info("------------------------------- 장바구니 저장");

        return itemService.addItemToCart(cartItemDto, memberDto);
    }

    @DeleteMapping("/cart/{cartItemId}")
    public Long deleteCartItem(@PathVariable Long cartItemId) {
        return cartItemId;
    }

    @GetMapping("/item/{itemId}")
    public ModelAndView toItemDetail(@PathVariable("itemId") Long itemId, @LoginMember MemberDto memberDto) {
        log.info("------------------------------- 상품 디테일 페이지 진입");

        ModelAndView mav = new ModelAndView();

        List<CouponDto> couponList = itemService.getUsableCouponList(itemId);
        List<ReviewDto> reviewList = postService.getReviewListByItemId(itemId);
        Map<Integer, Integer> starValMap = postService.getReviewScoreByItemId(itemId);
        ItemDto itemDto = itemService.getItemById(itemId);
        LikesResponseDto likes = postService.checkLike(itemId, memberDto);


        log.info("reviewList.size : ", reviewList.size());
        mav.addObject("couponList", couponList);
        mav.addObject("reviewList", reviewList);
        mav.addObject("starValMap", starValMap);
        mav.addObject("item", itemDto);
        mav.addObject("likes", likes);
        mav.setViewName("item/itemdetail");
        return mav;
    }

    @GetMapping("/item/men")
    public ModelAndView toMenItem() {
        log.info("------------------------------- 남성용 상품 페이지 진입");

        ModelAndView mav = new ModelAndView();
        mav.addObject("items", itemService.selectMenItems());
        mav.setViewName("item/men-item-list");
        return mav;
    }

    @GetMapping("/item/women")
    public ModelAndView toWomenItem() {
        log.info("------------------------------- 여성용 상품 페이지 진입");

        ModelAndView mav = new ModelAndView();
        mav.addObject("items", itemService.selectWomenItems());
        mav.setViewName("item/women-item-list");
        return mav;
    }

    @PostMapping("/item")
    public ModelAndView regItem(ItemRequestDto itemRequestDto) throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("member/mypage/admin/admin-registration");
        itemService.registerItemAndImgs(itemRequestDto);
        return mav;
    }

    @DeleteMapping("/cart/all")
    public String deleteCartItemAll(@RequestBody List<Long> cartItemIds) {
        log.info(String.valueOf(cartItemIds));
        return itemService.deleteCartItem(cartItemIds);
    }

    @PostMapping("cart/item/quantity")
    public String updateCartItemQuantity(@RequestBody CartItemRequestDto req) {

        log.info("------------------------상품 수량 업데이트");
        return itemService.updateCartItemQuantity(req);
    }

    @PostMapping("/coupon")
    public String createCoupon(@RequestBody CouponRequestDto requestDto) {

        log.info(requestDto.getDiscountFor().toString());
        log.info(String.valueOf(requestDto.getDiscountFor().getClass()));

        return itemService.createCoupon(requestDto);
    }

    @PostMapping("/member/coupon")
    public String saveUsableCoupon(@RequestBody CouponRequestDto requestDto) {
        return itemService.saveUsableCoupon(requestDto);
    }

    @GetMapping("/mypage/order/{sortBy}")
    public List<OrderItemDto> sortOrderItems(@PathVariable("sortBy") String sortBy, @LoginMember MemberDto memberDto) {
        log.info("----------------- sortBy : " + sortBy);
        List<OrderItemDto> itemList = orderService.sortByOrderState(sortBy, memberDto);
        log.info("itemList : " + itemList);
        return itemList;
    }

    @GetMapping("/item/men/{sortBy}")
    public List<ItemDto> sortMenItemsByCategory(@PathVariable String sortBy) {

        log.info("------------------------------ 카테고리 별 상품 요청");

        log.info(itemService.sortItemByCategory(sortBy).toString());
        return itemService.sortItemByCategory(sortBy);
    }
}
