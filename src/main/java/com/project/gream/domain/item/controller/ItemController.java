package com.project.gream.domain.item.controller;

import com.project.gream.common.annotation.LoginMember;
import com.project.gream.domain.item.dto.ItemRequestDto;
import com.project.gream.domain.item.dto.ItemVO;
import com.project.gream.domain.item.service.ItemService;
import com.project.gream.domain.member.dto.CartItemDto;
import com.project.gream.domain.member.dto.MemberVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ItemController {

    private final ItemService itemService;


    @GetMapping("/cart/{userId}")
    public ModelAndView toCart(@PathVariable("userId") String userId) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/item/cart");
        return mav;
    }

    @PostMapping("/cart")
    public String addItemToCart(@RequestBody CartItemDto cartItemDto, @LoginMember MemberVO memberVo) {


        return "성공";
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



}
