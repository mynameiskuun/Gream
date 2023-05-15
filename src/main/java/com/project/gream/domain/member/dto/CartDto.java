package com.project.gream.domain.member.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.project.gream.domain.member.entity.Cart;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class CartDto {

    private Long id;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdTime;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime modifiedTime;

    public Cart toEntity() {
        return Cart.builder()
                .id(this.id)
                .build();
    }
    public static CartDto fromEntity(Cart cart) {
        return CartDto.builder()
                .id(cart.getId())
                .build();
    }

    @Builder
    public CartDto(Long id, LocalDateTime createdTime, LocalDateTime modifiedTime) {
        this.id = id;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
    }
}
