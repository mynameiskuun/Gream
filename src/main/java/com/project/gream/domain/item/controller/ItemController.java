package com.project.gream.domain.item.controller;

import com.project.gream.common.annotation.LoginMember;
import com.project.gream.domain.item.dto.ItemRequestDto;
import com.project.gream.domain.item.dto.ItemVO;
import com.project.gream.domain.item.service.ItemService;
import com.project.gream.domain.member.dto.CartItemDto;
import com.project.gream.domain.member.dto.MemberVO;
import com.project.gream.domain.member.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ItemController {

    private final ItemService itemService;
    private final CartItemRepository cartItemRepository;

    @PostMapping("/cart")
    public String addItemToCart(@RequestBody CartItemDto cartItemDto, @LoginMember MemberVO memberVo) {
        return itemService.addItemToCart(cartItemDto, memberVo);
    }

    @DeleteMapping("/cart/{cartItemId}")
    public Long deleteCartItem(@PathVariable Long cartItemId) {
//        cartItemRepository.deleteById(cartItemId);
        return cartItemId;
    }

    @GetMapping("/item/{itemId}")
    public ModelAndView toItemDetail(@PathVariable("itemId") Long itemId) {
        ModelAndView mav = new ModelAndView();
        ItemVO itemVO = itemService.getItemById(itemId);
        mav.addObject("item", itemVO);
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
        mav.setViewName("mypage/admin/admin-registration");
        itemService.registerItemAndImgs(itemRequestDto);
        return mav;
    }

    @DeleteMapping("/cart/all")
    public String deleteCartItemAll(@RequestBody List<Long> cartItemIds) {
        log.info(String.valueOf(cartItemIds));
        return itemService.deleteCartItem(cartItemIds);
    }



}
