package com.project.gream.domain.order.controller;

import com.project.gream.domain.item.service.ItemService;
import com.project.gream.domain.member.entity.CartItem;
import com.project.gream.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class OrderController {

    private final ItemService itemService;
    private final MemberService memberService;


    @GetMapping("/cart/order")
    public ModelAndView toOrderFromCart(@RequestBody List<CartItem> orderItems) {

        return new ModelAndView();
    }
}
