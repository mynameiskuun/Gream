package com.project.gream.domain.order.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoPayResultVO {

    private int total;
    private int tax_free;
    private int vat;
    private int point;
    private int discount;

}
