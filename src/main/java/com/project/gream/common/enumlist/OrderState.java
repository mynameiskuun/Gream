package com.project.gream.common.enumlist;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OrderState implements EnumBase{

    PREPARE("배송 준비중"),
    ONPROGRESS("배송중"),
    ARRIVED("배송 완료"),
    CONFIRMED("구매 확정");

    private final String value;
    public String getValue() {
        return this.value;
    }
}
