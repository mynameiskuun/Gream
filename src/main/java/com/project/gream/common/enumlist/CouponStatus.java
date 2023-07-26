package com.project.gream.common.enumlist;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CouponStatus implements EnumBase{

    VALID("사용 가능"),
    INVALID("사용 완료"),
    EXPIRED("기간 만료");

    private final String value;
    @Override
    public String getValue() {
        return this.value;
    }
}
