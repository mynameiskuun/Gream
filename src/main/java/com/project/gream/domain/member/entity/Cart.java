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
public class Cart extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    @Builder
    public Cart(Long id) {
        this.id = id;
    }
}
