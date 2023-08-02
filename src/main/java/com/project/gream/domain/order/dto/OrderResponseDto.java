package com.project.gream.domain.order.dto;

import com.project.gream.common.enumlist.OrderState;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class OrderResponseDto {

    private Long orderItemId;
    private Long orderHistoryId;
    private String memberId;
    private String orderItemName;
    private int orderItemQuantity;
    private Long paymentAmount;
    private Long couponDiscountAmount;
    private String address;
    private String message;
    private OrderState orderState;

    @Builder
    public OrderResponseDto(Long orderItemId, Long orderHistoryId, String memberId, String orderItemName, int orderItemQuantity,
                            Long paymentAmount, Long couponDiscountAmount, String address, String message, OrderState orderState) {
        this.orderItemId = orderItemId;
        this.orderHistoryId = orderHistoryId;
        this.orderItemName = orderItemName;
        this.orderItemQuantity = orderItemQuantity;
        this.paymentAmount = paymentAmount;
        this.couponDiscountAmount = couponDiscountAmount;
        this.memberId = memberId;
        this.address = address;
        this.message = message;
        this.orderState = orderState;
    }
}
