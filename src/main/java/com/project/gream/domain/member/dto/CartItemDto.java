package com.project.gream.domain.member.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.project.gream.domain.item.dto.ItemVO;
import com.project.gream.domain.member.entity.CartItem;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CartItemDto {

    private Long id;
    private ItemVO itemVo;
    private CartDto cartDto;
    private int quantity;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdTime;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime modifiedTime;

    @Builder
    public CartItemDto(Long id, ItemVO itemVo, CartDto cartDto, int quantity, LocalDateTime createdTime, LocalDateTime modifiedTime) {
        this.id = id;
        this.itemVo = itemVo;
        this.cartDto = cartDto;
        this.quantity = quantity;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
    }

    public CartItem toEntity() {
        return CartItem.builder()
                .id(id)
                .item(itemVo.toEntity())
                .cart(cartDto.toEntity())
                .quantity(quantity)
                .build();
    }
}
