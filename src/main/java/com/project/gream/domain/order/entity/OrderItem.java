package com.project.gream.domain.order.entity;

import com.project.gream.common.util.BaseTimeEntity;
import com.project.gream.domain.item.entity.Item;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class OrderItem extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderitem_id")
    private Long id;
    private int quantity;
    private int totalPrice;
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
    @ManyToOne
    @JoinColumn(name = "orderhistory_id")
    private OrderHistory orderHistory;

    @Builder
    public OrderItem(Long id, OrderHistory orderHistory, int quantity, int totalPrice, Item item) {
        this.id = id;
        this.orderHistory = orderHistory;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.item = item;
    }
}
