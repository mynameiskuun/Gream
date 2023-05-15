package com.project.gream.domain.member.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.project.gream.domain.item.dto.ItemDto;
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
    private ItemDto itemDto;
    private CartDto cartDto;
    private int qty;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdTime;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime modifiedTime;

    @Builder
    public CartItemDto(Long id, ItemDto itemDto, CartDto cartDto, int qty, LocalDateTime createdTime, LocalDateTime modifiedTime) {
        this.id = id;
        this.itemDto = itemDto;
        this.cartDto = cartDto;
        this.qty = qty;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
    }
}
