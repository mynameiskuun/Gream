package com.project.gream.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.project.gream.domain.item.dto.ItemDto;
import com.project.gream.domain.member.entity.CartItem;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class CartItemDto {

    private Long id;
    private ItemDto itemDto;
    private CartDto cartDto;
    private int quantity;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
//    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdTime;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
//    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime modifiedTime;


    public CartItem toEntity() {
        return CartItem.builder()
                .id(id)
                .item(itemDto.toEntity())
                .cart(cartDto.toEntity())
                .quantity(quantity)
                .build();
    }

    public static CartItemDto fromEntity(CartItem cartItem) {
        return CartItemDto.builder()
                .id(cartItem.getId())
                .itemDto(ItemDto.fromEntity(cartItem.getItem()))
                .cartDto(CartDto.fromEntity(cartItem.getCart()))
                .quantity(cartItem.getQuantity())
                .createdTime(cartItem.getCreatedTime())
                .modifiedTime(cartItem.getModifiedTime())
                .build();
    }

    @Builder
    public CartItemDto(Long id, ItemDto itemDto, CartDto cartDto, int quantity, LocalDateTime createdTime, LocalDateTime modifiedTime) {
        this.id = id;
        this.itemDto = itemDto;
        this.cartDto = cartDto;
        this.quantity = quantity;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
    }

}
