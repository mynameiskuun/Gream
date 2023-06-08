package com.project.gream.domain.item.controller;

import com.project.gream.common.annotation.LoginMember;
import com.project.gream.domain.item.dto.CartItemRequestDto;
import com.project.gream.domain.item.dto.ItemRequestDto;
import com.project.gream.domain.item.dto.ItemDto;
import com.project.gream.domain.item.service.ItemService;
import com.project.gream.domain.member.dto.CartItemDto;
import com.project.gream.domain.member.dto.MemberDto;
import com.project.gream.domain.member.repository.CartItemRepository;
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

    @PostMapping("/cart")
    public String addItemToCart(@RequestBody CartItemDto cartItemDto, @LoginMember MemberDto memberDto) {
        return itemService.addItemToCart(cartItemDto, memberDto);
    }

    @DeleteMapping("/cart/{cartItemId}")
    public Long deleteCartItem(@PathVariable Long cartItemId) {
//        cartItemRepository.deleteById(cartItemId);
        return cartItemId;
    }

    @GetMapping("/item/{itemId}")
    public ModelAndView toItemDetail(@PathVariable("itemId") Long itemId, @LoginMember MemberDto memberDto) {
        ModelAndView mav = new ModelAndView();
        ItemDto itemDto = itemService.getItemById(itemId);

//        postService.saveReviewsForTest(memberDto);
        Map<Integer, Integer> starValMap = postService.getReviewScoreByItemId(itemId);
        List<ReviewDto> reviewList = postService.getReviewDtoById(itemId);

        mav.addObject("reviewList", reviewList);
        mav.addObject("starValMap", starValMap);
        mav.addObject("item", itemDto);
        mav.setViewName("/item/itemdetail");
        return mav;
    }

    @GetMapping("/item/men")
    public ModelAndView toMenItem() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("items", itemService.selectMenItems());
        mav.setViewName("item/men-item-list");
        return mav;
    }

    @GetMapping("/item/women")
    public ModelAndView toWomenItem() {
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


}
