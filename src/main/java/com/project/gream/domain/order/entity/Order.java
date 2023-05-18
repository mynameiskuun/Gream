package com.project.gream.domain.order.entity;

import com.project.gream.common.util.BaseTimeEntity;
import com.project.gream.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.context.annotation.Lazy;

import javax.persistence.*;

@Getter
@DynamicInsert
@NoArgsConstructor
@Entity
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @Column(columnDefinition = "varchar (100) default '배송 준비중'")
    private String state;
    private int usePoint;
    private int totalOrderPrice;

    @Builder
    public Order(Long id, Member member, String state, int usePoint, int totalOrderPrice) {
        this.id = id;
        this.member = member;
        this.state = state;
        this.usePoint = usePoint;
        this.totalOrderPrice = totalOrderPrice;
    }
}
