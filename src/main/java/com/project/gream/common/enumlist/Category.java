package com.project.gream.common.enumlist;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Category implements EnumBase{

    OUTER("아우터"),
    TSHIRTS ("티셔츠"),
    SWEATSHIRTS("스웨트셔츠"),
    SHIRTS( "셔츠"),
    KNIT("니트"),
    DRESS( "원피스"),
    BOTTOM( "하의"),
    SHOES("신발"),
    ACCESSORIES("악세사리");

    private final String value;

}
