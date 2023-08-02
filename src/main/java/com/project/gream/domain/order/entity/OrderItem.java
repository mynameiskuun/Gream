package com.project.gream.domain.order.entity;

import com.project.gream.common.enumlist.OrderState;
import com.project.gream.common.enumlist.converter.OrderStateConverter;
import com.project.gream.common.util.BaseTimeEntity;
import com.project.gream.domain.item.entity.Item;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@NoArgsConstructor
@DynamicInsert // 실제 DB에 save 되는 시점에 영향을 미침. 이 설정을 통해 insert 시 null인 필드를 제외함.
@Getter
@Entity
public class OrderItem extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderitem_id")
    private Long id;
    @Column(nullable = false)
    private int quantity;
    @Column(nullable = false)
    private int totalPrice;
    @Column(columnDefinition = "integer default 0")
    private Long couponDiscountAmount;
    @Convert(converter = OrderStateConverter.class)
    @Column(columnDefinition = "varchar (100) default '배송 준비중'") // 테이블 생성 시점에 DDL 생성. 실제 db에 업데이트 되는 시점에는 영향 X.
    private OrderState state;
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
    @ManyToOne
    @JoinColumn(name = "orderhistory_id")
    private OrderHistory orderHistory;

    @Builder
    public OrderItem(Long id, int quantity, int totalPrice, Long couponDiscountAmount,
                     OrderState state, Item item, OrderHistory orderHistory) {
        this.id = id;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.couponDiscountAmount = couponDiscountAmount;
        this.state = state;
        this.item = item;
        this.orderHistory = orderHistory;
    }

    public void updateState(OrderState state) {
        this.state = state;
    }
}
