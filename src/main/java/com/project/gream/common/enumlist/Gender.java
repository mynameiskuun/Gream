package com.project.gream.common.enumlist;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Gender implements EnumBase{

    MAN("남성"),
    WOMAN( "여성");

    private final String value;
}
