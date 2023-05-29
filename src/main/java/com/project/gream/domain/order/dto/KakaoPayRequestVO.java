package com.project.gream.domain.order.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoPayRequestVO {

    private String tid;
    private String next_redirect_pc_url;
    private String partner_order_id;

}
