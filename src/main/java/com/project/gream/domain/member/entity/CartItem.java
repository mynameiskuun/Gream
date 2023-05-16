package com.project.gream.domain.member.entity;

import com.project.gream.common.util.BaseTimeEntity;
import com.project.gream.domain.item.entity.Item;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class CartItem extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cartitem_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;
    @Column(name = "cartitem_qty")
    private int quantity;

    public void updateCartIemQty(int quantity) {
        this.quantity = quantity;}

    @Builder
    public CartItem(Long id, Item item, Cart cart, int quantity) {
        this.id = id;
        this.item = item;
        this.cart = cart;
        this.quantity = quantity;
    }

}
