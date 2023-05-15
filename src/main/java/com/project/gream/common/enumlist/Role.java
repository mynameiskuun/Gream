package com.project.gream.common.enumlist;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Role implements EnumBase{

    MEMBER("ROLE_MEMBER", "회원"),
    ADMIN("ROLE_ADMIN", "관리자");

    private final String value;
    private final String key;
}
