package com.project.gream.domain.member.entity;

import com.project.gream.common.util.BaseTimeEntity;
import com.project.gream.domain.item.entity.Item;
import com.project.gream.domain.member.entity.Cart;
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
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="cart_id")
    private Cart cart;
    @Column(nullable = false)
    private int quantity;

    public void updateCartItemQty(int quantity) {
        this.quantity = quantity;
    }

    @Builder
    public CartItem(Long id, Item item, Cart cart, int quantity) {
        this.id = id;
        this.item = item;
        this.cart = cart;
        this.quantity = quantity;
    }

}

