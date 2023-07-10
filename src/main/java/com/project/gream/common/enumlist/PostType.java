package com.project.gream.common.enumlist;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PostType implements EnumBase{

    QNA("문의"),
    ANSWER("답변"),
    COMPLETED("답변 완료"),
    NOTICE("공지사항");

    private final String value;
}
