package com.project.gream.common.enumlist;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum QnaType implements EnumBase{

    PRODUCT("상품"),
    DELIVERY("배송"),
    EXCHANGE("교환"),
    ETC("기타");

    private final String value;
}
