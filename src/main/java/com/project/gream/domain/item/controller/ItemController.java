package com.project.gream.domain.item.controller;

import com.project.gream.common.annotation.LoginMember;
import com.project.gream.domain.item.dto.*;
import com.project.gream.domain.item.service.DiscountService;
import com.project.gream.domain.item.service.ItemService;
import com.project.gream.domain.member.dto.CartItemDto;
import com.project.gream.domain.member.dto.MemberDto;
import com.project.gream.domain.member.repository.CartItemRepository;
import com.project.gream.domain.order.dto.OrderHistoryDto;
import com.project.gream.domain.order.dto.OrderItemDto;
import com.project.gream.domain.order.service.OrderService;
import com.project.gream.domain.post.dto.LikesResponseDto;
import com.project.gream.domain.post.dto.ReviewDto;
import com.project.gream.domain.post.entity.Post;
import com.project.gream.domain.post.repository.ReviewRepository;
import com.project.gream.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ItemController {

    private final ItemService itemService;
    private final PostService postService;
    private final OrderService orderService;
    private final DiscountService discountService;

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
    public ModelAndView toItemDetail(@PathVariable("itemId") Long itemId,
                                     @LoginMember MemberDto memberDto,
                                     @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) @Qualifier("qna") Pageable qnaPageable,
                                     @PageableDefault(page = 0, size = 2, sort = "id", direction = Sort.Direction.DESC) @Qualifier("review") Pageable reviewPageable) {
        log.info("------------------------------- 상품 디테일 페이지 진입");

        ModelAndView mav = new ModelAndView();

        List<CouponDto> couponList = discountService.getUsableCouponList(itemId);
        Map<Integer, Integer> starValMap = postService.getReviewScoreByItemId(itemId);
        ItemDto itemDto = itemService.getItemById(itemId);
        LikesResponseDto likes = postService.checkLike(itemId, memberDto);
        Page<ReviewDto> reviewList = postService.getReviewListByItemId(itemId, reviewPageable);
        Page<Post> qnaList = postService.getQnaListByItemId(itemId, qnaPageable);

        int reviewNowPage = reviewList.getPageable().getPageNumber() + 1;
        int reviewStartPage = Math.max(reviewNowPage - 4, 1);
        int reviewEndPage = Math.min(reviewNowPage + 9, reviewList.getTotalPages());

        int qnaNowPage = qnaList.getPageable().getPageNumber() + 1;
        int qnaStartPage = Math.max(qnaNowPage - 4, 1);
        int qnaEndPage = Math.min(qnaNowPage + 9, qnaList.getTotalPages());

        log.info("reviewList.size : " + reviewList.getSize());
        log.info("qnaList.size : " + qnaList.stream().count());

        mav.addObject("reviewNowPage", reviewNowPage);
        mav.addObject("reviewStartPage", reviewStartPage);
        mav.addObject("reviewEndPage", reviewEndPage);

        mav.addObject("qnaNowPage", qnaNowPage);
        mav.addObject("qnaStartPage", qnaStartPage);
        mav.addObject("qnaEndPage", qnaEndPage);

        mav.addObject("qnaList", qnaList);
        mav.addObject("couponList", couponList);
        mav.addObject("reviewList", reviewList);
        mav.addObject("starValMap", starValMap);
        mav.addObject("item", itemDto);
        mav.addObject("likes", likes);
        mav.setViewName("item/itemdetail");
        return mav;
    }

    @GetMapping("/item/men")
    public ModelAndView toMenItem(@PageableDefault(page = 0, size = 8, sort = "id",  direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("------------------------------- 남성용 상품 페이지 진입");

        Page<ItemDto> manItemList = itemService.selectMenItems(pageable);
        int nowPage = manItemList.getPageable().getPageNumber() + 1;
        int startPage =  Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 9, manItemList.getTotalPages());

        ModelAndView mav = new ModelAndView();
        mav.addObject("nowPage", nowPage);
        mav.addObject("startPage", startPage);
        mav.addObject("endPage", endPage);
        mav.addObject("items", manItemList);
        mav.setViewName("item/men-item-list");
        return mav;
    }

    @GetMapping("/item/women")
    public ModelAndView toWomenItem(@PageableDefault(page = 0, size = 8, sort = "id",  direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("------------------------------- 여성용 상품 페이지 진입");

        ModelAndView mav = new ModelAndView();
        Page<ItemDto> womanItemList = itemService.selectWomenItems(pageable);
        int nowPage = womanItemList.getPageable().getPageNumber() + 1;
        int startPage =  Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 9, womanItemList.getTotalPages());

        mav.addObject("nowPage", nowPage);
        mav.addObject("startPage", startPage);
        mav.addObject("endPage", endPage);
        mav.addObject("items", itemService.selectWomenItems(pageable));
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

    @PostMapping("/cart/item/quantity")
    public String updateCartItemQuantity(@RequestBody CartItemRequestDto req) {

        log.info("------------------------상품 수량 업데이트");
        return itemService.updateCartItemQuantity(req);
    }

    @PostMapping("/coupon")
    public String createCoupon(@RequestBody CouponRequestDto requestDto) {

        log.info(requestDto.getDiscountFor().toString());
        log.info(String.valueOf(requestDto.getDiscountFor().getClass()));

        return discountService.createCoupon(requestDto);
    }

    @PostMapping("/member/coupon")
    public String saveUsableCoupon(@RequestBody CouponRequestDto requestDto) {
        return discountService.saveUsableCoupon(requestDto);
    }

    @GetMapping("/mypage/order/{sortBy}")
    public List<OrderItemDto> sortOrderItems(@PathVariable("sortBy") String sortBy, @LoginMember MemberDto memberDto) {
        log.info("----------------- sortBy : " + sortBy);
        List<OrderItemDto> itemList = orderService.sortByOrderState(sortBy, memberDto);
        log.info("itemList : " + itemList);
        return itemList;
    }

    @GetMapping("/item/{gender}/{sortBy}")
    public List<ItemDto> sortWomenItemsByCategory(@PathVariable("gender") String gender,
                                                  @PathVariable("sortBy") String sortBy,
                                                  @PageableDefault(size = 8, page = 0, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        log.info("------------------------------ 카테고리 별 상품 요청");

        return itemService.sortItemByGenderAndCategory(gender, sortBy);
    }

    @GetMapping("/point/check/{inputPoint}")
    public Map<String, String> isPointUsable(@PathVariable("inputPoint") int point, @LoginMember MemberDto memberDto) {

        return discountService.isPointUsable(point, memberDto);
    }

    @GetMapping("/member/coupon/popup/{category}")
    public List<UserCouponResponseDto> getUserCouponForItem(@PathVariable("category") String category,
                                                            @LoginMember MemberDto memberDto) {

        return discountService.getUserCouponForItem(category, memberDto);
    }

    @PostMapping("/apply-coupon")
    public ResponseEntity<UserCouponDto.CouponApplyResponse> couponValidation(@RequestBody UserCouponDto.CouponApplyRequest request) {

        UserCouponDto.CouponApplyResponse response = discountService.applyCoupon(request);
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        return new ResponseEntity<>(response, header, HttpStatus.OK);
    }
}
