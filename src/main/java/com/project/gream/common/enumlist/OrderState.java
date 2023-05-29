package com.project.gream.common.enumlist;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OrderState {

    PREPARE("배송 준비중"),
    ONPROGRESS("배송중"),
    ARRIVED("배송 완료");

    private final String value;
    public String getValue() {
        return this.value;
    }
}
