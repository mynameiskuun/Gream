package com.project.gream.domain.order.service;

import com.project.gream.common.annotation.LoginMember;
import com.project.gream.domain.item.dto.ItemDto;
import com.project.gream.domain.member.dto.MemberDto;
import com.project.gream.domain.member.entity.Member;
import com.project.gream.domain.order.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {

    KakaoPayRequestVO kakaoPayReady(KakaoPayDto request);
    KakaoPayApprovedResultVO payApprove(String tid, String pgToken, KakaoPayDto request);
    void updateDataBaseAfterPayment(KakaoPayDto kakaoPayDto, @LoginMember MemberDto memberDto);
    void sendPaymentReceiptEmail(KakaoPayApprovedResultVO result, @LoginMember MemberDto memberDto, Long totalDiscountAmount) throws Exception;
    List<OrderItemDto> findOrderItemForMypage(String memberId);
    Page<OrderItemDto> findAllOrderItem(String memberId, Pageable pageable);
    List<OrderItemDto> sortByOrderState(String sortBy, MemberDto memberDto);

    List<OrderHistoryDto> findTop5OrderByCreatedTimeDesc();

    Page<OrderResponseDto> getAllOrderItemsForAdmin(Pageable pageable);

    OrderResponseDto changeOrderStatus(OrderRequestDto request);
}
