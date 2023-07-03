package com.project.gream.common.enumlist;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PostType implements EnumBase{

    QNA("문의"),
    NOTICE("공지사항");

    private final String value;
}
