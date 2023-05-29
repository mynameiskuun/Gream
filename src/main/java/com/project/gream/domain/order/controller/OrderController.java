package com.project.gream.domain.order.controller;

import com.project.gream.common.annotation.LoginMember;
import com.project.gream.domain.item.dto.ItemDto;
import com.project.gream.domain.item.entity.Item;
import com.project.gream.domain.item.service.ItemService;
import com.project.gream.domain.member.dto.CartItemDto;
import com.project.gream.domain.member.dto.MemberDto;
import com.project.gream.domain.member.entity.CartItem;
import com.project.gream.domain.member.repository.CartItemRepository;
import com.project.gream.domain.member.service.MemberService;
import com.project.gream.domain.order.dto.*;
import com.project.gream.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@SessionAttributes({"tid" , "kakaoPayDto"})
@RequiredArgsConstructor
@RestController
public class OrderController {

    private final ItemService itemService;
    private final MemberService memberService;
    private final OrderService orderService;


    @PostMapping("/cart/order")
    public ModelAndView toOrderFromCart(String cartItemIds) {
        ModelAndView mav = new ModelAndView();

        log.info("------------------ 장바구니에서 주문화면으로");
        log.info("parameter : " + cartItemIds);

        List<CartItemDto> cartItemList = itemService.getOrderItemsFromCart(cartItemIds);
        mav.addObject("orderItemList", cartItemList);
        mav.setViewName("order/order");
        return mav;
    }

    @PostMapping("/order")
    public ModelAndView toOrderFromDetail(ItemDto itemDto, int orderQuantity) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("itemDto", itemDto);
        mav.addObject("orderQuantity", orderQuantity);
        mav.setViewName("order/order");
        return mav;
    }

    @PostMapping("/order/kakaopay")
    public KakaoPayRequestVO requestKakaoPay(@RequestBody KakaoPayDto request, Model model) {

        KakaoPayRequestVO response = orderService.kakaoPayReady(request);

        model.addAttribute("kakaoPayDto", request);
        model.addAttribute("tid", response.getTid());

        return response;
    }

    @GetMapping("/order/kakaopay/authorization")
    public ModelAndView paymentCompleted(@RequestParam("pg_token") String pgToken, @ModelAttribute("tid") String tid,
                                   @ModelAttribute("kakaoPayDto") KakaoPayDto request, @LoginMember MemberDto memberDto) throws Exception {

        KakaoPayApprovedResultVO response = orderService.payApprove(tid, pgToken, request);
        orderService.updateDataBaseAfterPayment(request, memberDto);
        orderService.sendPaymentReceiptEmail(response, memberDto);

        log.info("------------------------------------ orderResult ===> " + String.valueOf(response));

        ModelAndView mav = new ModelAndView();
        mav.setViewName("order/order-success");
        mav.addObject("orderResult", response);

        return mav;
    }

    @GetMapping("/order/pay/cancel")
    public String toResult() {
        return "order/order-cancel";
    }

    @GetMapping("/order/pay/fail")
    public String toCancel() {
        return "order/order-fail";
    }
}
