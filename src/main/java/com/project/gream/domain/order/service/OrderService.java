package com.project.gream.domain.order.service;

import com.project.gream.common.annotation.LoginMember;
import com.project.gream.domain.member.dto.MemberDto;
import com.project.gream.domain.order.dto.KakaoPayApprovedResultVO;
import com.project.gream.domain.order.dto.KakaoPayDto;
import com.project.gream.domain.order.dto.KakaoPayRequestVO;
import com.project.gream.domain.order.dto.OrderItemDto;
import com.project.gream.domain.order.entity.OrderItem;

import java.util.List;

public interface OrderService {

    KakaoPayRequestVO kakaoPayReady(KakaoPayDto request);

    KakaoPayApprovedResultVO payApprove(String tid, String pgToken, KakaoPayDto request);

    void updateDataBaseAfterPayment(KakaoPayDto kakaoPayDto, @LoginMember MemberDto memberDto);

    void sendPaymentReceiptEmail(KakaoPayApprovedResultVO result, @LoginMember MemberDto memberDto) throws Exception;

    List<OrderItemDto> findOrderItem(String memberId);
}
