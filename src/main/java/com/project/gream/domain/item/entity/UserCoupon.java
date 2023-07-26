package com.project.gream.domain.item.entity;

import com.project.gream.common.enumlist.CouponStatus;
import com.project.gream.common.enumlist.OrderState;
import com.project.gream.common.enumlist.converter.CouponStatusConverter;
import com.project.gream.common.util.BaseTimeEntity;
import com.project.gream.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;

@DynamicInsert
@Getter
@NoArgsConstructor
@Entity
public class UserCoupon extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usercoupon_id")
    private Long id;
    @Convert(converter = CouponStatusConverter.class)
    @Column(columnDefinition = "varchar (100) default '사용 가능'") // 테이블 생성 시점에 DDL 생성. 실제 db에 업데이트 되는 시점에는 영향 X.
    private CouponStatus status;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public UserCoupon(Long id, CouponStatus status, Coupon coupon, Member member) {
        this.id = id;
        this.status = status;
        this.coupon = coupon;
        this.member = member;
    }

    public void setStatus(CouponStatus couponStatus) {
        this.status = couponStatus;
    }
}
