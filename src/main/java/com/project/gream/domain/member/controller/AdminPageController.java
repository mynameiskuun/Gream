package com.project.gream.domain.member.controller;

import com.project.gream.domain.item.dto.CouponDto;
import com.project.gream.domain.item.dto.ItemDto;
import com.project.gream.domain.item.repository.CouponRepository;
import com.project.gream.domain.item.service.DiscountService;
import com.project.gream.domain.item.service.ItemService;
import com.project.gream.domain.member.repository.CartItemRepository;
import com.project.gream.domain.member.repository.MemberRepository;
import com.project.gream.domain.order.dto.OrderHistoryDto;
import com.project.gream.domain.order.dto.OrderRequestDto;
import com.project.gream.domain.order.dto.OrderResponseDto;
import com.project.gream.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.nio.charset.Charset;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin")
@RestController
public class AdminPageController {

    private final ItemService itemService;
    private final OrderService orderService;
    private final DiscountService discountService;

    @GetMapping("/main")
    public ModelAndView toAdmin() {
        ModelAndView mav = new ModelAndView();
        List<OrderHistoryDto> orderHistoryList = orderService.findTop5OrderByCreatedTimeDesc();
        mav.addObject("orderHistoryList", orderHistoryList);
        mav.setViewName("member/mypage/admin/admin-main");
        return mav;
    }

    @GetMapping("/orderlist")
    public ModelAndView toOrderList(@PageableDefault(size = 10, page = 0, direction = Sort.Direction.DESC) Pageable pageable) {
        ModelAndView mav = new ModelAndView();

        Page<OrderResponseDto> orderList = orderService.getAllOrderItemsForAdmin(pageable);
        int nowPage = orderList.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 9, orderList.getTotalPages());

        log.info("nowPage : " + nowPage);
        log.info("startPage : " + startPage);
        log.info("endPage : " + endPage);
        log.info("getTotalPages : " + orderList.getTotalPages());

        mav.addObject("orderList", orderList);
        mav.addObject("nowPage", nowPage);
        mav.addObject("startPage", startPage);
        mav.addObject("endPage", endPage);

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

    @GetMapping("/coupon/create")
    public ModelAndView toCouponCreate() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("member/mypage/admin/create-coupon");
        return mav;
    }

    @GetMapping("/coupon")
    public ModelAndView toCouponManage(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<CouponDto> couponList = discountService.getCouponList(pageable);
        ModelAndView mav = new ModelAndView();
        int nowPage = couponList.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 9, couponList.getTotalPages());

        for (CouponDto c : couponList) {
            log.info(String.valueOf(c.getCreatedTime()));
        }
        mav.addObject("nowPage", nowPage);
        mav.addObject("startPage", startPage);
        mav.addObject("endPage", endPage);
        mav.addObject("couponList", couponList);
        mav.setViewName("member/mypage/admin/admin-couponlist");
        return mav;
    }

    @GetMapping("/inquiry")
    public ModelAndView toInquiry() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("member/mypage/admin/admin-inquiry");
        return mav;
    }

    @DeleteMapping("/coupon/{couponId}")
    public String deleteCoupon(@PathVariable Long couponId) {
        log.info("couponId : " + couponId);
        return discountService.deleteCoupon(couponId);
    }

    @PatchMapping("/order/status")
    public ResponseEntity<OrderResponseDto> changeOrderStatus(@RequestBody OrderRequestDto request) {

        OrderResponseDto response = orderService.changeOrderStatus(request);
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        return new ResponseEntity(response, header, HttpStatus.OK);
    }
}
