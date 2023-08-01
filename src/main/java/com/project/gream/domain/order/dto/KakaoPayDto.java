package com.project.gream.domain.order.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class KakaoPayDto {

    private String name;
    private String tel;
    private String email;
    private int usePoint;
    private int couponDiscountAmount;
    private int pointRewardAmount;
    private int size;
    private String itemName;
    private long finalPaymentAmount;
    private List<Long> itemIds;
    private List<Integer> itemQtys;
    private List<Long> cartItemIds;
    private List<Long> usedCouponIds;

}
