package com.project.gream.domain.item.dto;

import com.project.gream.domain.member.dto.CartItemDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CartItemRequestDto {

    private int changedQuantity;
    private Long cartItemId;

    @Builder
    public CartItemRequestDto(int changedQuantity, Long cartItemId) {
        this.changedQuantity = changedQuantity;
        this.cartItemId = cartItemId;
    }

}
