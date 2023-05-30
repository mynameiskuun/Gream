package com.project.gream.domain.order.entity;

import com.project.gream.common.util.BaseTimeEntity;
import com.project.gream.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class OrderHistory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderhistory_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    private int usePoint;
    private Long totalOrderPrice;

    @Builder
    public OrderHistory(Long id, Member member, int usePoint, Long totalOrderPrice) {
        this.id = id;
        this.member = member;
        this.usePoint = usePoint;
        this.totalOrderPrice = totalOrderPrice;
    }
}
