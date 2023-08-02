package com.project.gream.common.enumlist;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OrderState implements EnumBase{

    ORDER_COMPLETED("주문 완료"),
    PREPARE("배송 준비중"),
    ONPROGRESS("배송중"),
    ARRIVED("배송 완료"),
    CONFIRMED("구매 확정"),
    REVIEWED("리뷰 작성 완료"),
    CANCEL_REQUESTED("취소 요청중"),
    CANCELED("취소 완료");

    // 상태 추가가 일어나더라도, Enum클래스에 상태를 추가하면 끝.
    // 이런 배송 상태에 관련한 데이터들은 변경이 잦지 않음.
    // 테이블의 컬럼을 변경하는 일은 생각보다 까다로움. 리스크가 큼.
    // DB 의존적인 개발에서 자유로울 수 있음.
    private final String value;
    public String getValue() {
        return this.value;
    }
}
