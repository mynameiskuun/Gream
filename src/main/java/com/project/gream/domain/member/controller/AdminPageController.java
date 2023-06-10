package com.project.gream.domain.member.controller;

import com.project.gream.domain.item.dto.ItemDto;
import com.project.gream.domain.item.service.ItemService;
import com.project.gream.domain.member.repository.CartItemRepository;
import com.project.gream.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin")
@RestController
public class AdminPageController {

    private final ItemService itemService;

    @GetMapping("/main")
    public ModelAndView toAdmin() {
        ModelAndView mav = new ModelAndView();
        List<ItemDto> itemList = itemService.selectAllItems();
        mav.addObject("itemList", itemList);
        mav.setViewName("member/mypage/admin/admin-main");
        return mav;
    }

    @GetMapping("/orderlist")
    public ModelAndView toOrderList() {
        ModelAndView mav = new ModelAndView();

        mav.setViewName("member/mypage/admin/admin-orderlist");
        return mav;
    }

    @GetMapping("/income")
    public ModelAndView toIncome() {
        ModelAndView mav = new ModelAndView();

        mav.setViewName("member/mypage/admin/admin-income");
        return mav;
    }

    @GetMapping("/item-management")
    public ModelAndView toManagement() {
        ModelAndView mav = new ModelAndView();

        mav.setViewName("member/mypage/admin/admin-items");
        return mav;
    }

    @GetMapping("/item-registration")
    public ModelAndView toRegitem() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("member/mypage/admin/admin-registration");
        return mav;
    }

    @GetMapping("/coupon")
    public ModelAndView toCouponCreate() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("member/mypage/admin/create-coupon");
        return mav;
    }

    @GetMapping("/coupon-management")
    public ModelAndView toCouponManage() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("member/mypage/admin/admin-couponlist");
        return mav;
    }

    @GetMapping("/inquiry")
    public ModelAndView toInquiry() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("member/mypage/admin/admin-inquiry");
        return mav;
    }

}
