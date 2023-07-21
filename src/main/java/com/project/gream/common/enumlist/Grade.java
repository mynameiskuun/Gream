package com.project.gream.common.enumlist;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Grade implements EnumBase {

    BRONZE("0.03"),
    SILVER("0.05"),
    GOLD("0.07");

    private final String value;
}
