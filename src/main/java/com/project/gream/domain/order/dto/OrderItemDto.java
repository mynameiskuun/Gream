package com.project.gream.domain.order.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.project.gream.domain.item.dto.ItemDto;
import com.project.gream.domain.order.entity.OrderItem;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.criterion.Order;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class OrderItemDto {

    private Long id;
    private int quantity;
    private int totalPrice;
    private String state;
    private ItemDto itemDto;
    private OrderHistoryDto orderHistoryDto;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
//    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdTime;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
//    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime modifiedTime;

    @Builder
    public OrderItemDto(Long id, int quantity, int totalPrice, String state, ItemDto itemDto,
                        OrderHistoryDto orderHistoryDto, LocalDateTime createdTime, LocalDateTime modifiedTime) {
        this.id = id;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.state = state;
        this.itemDto = itemDto;
        this.orderHistoryDto = orderHistoryDto;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
    }

    public OrderItem toEntity() {
        return OrderItem.builder()
                .id(id)
                .quantity(quantity)
                .totalPrice(totalPrice)
                .state(state)
                .item(itemDto.toEntity())
                .orderHistory(orderHistoryDto.toEntity())
                .build();
    }

    public static OrderItemDto fromEntity(OrderItem orderItem) {
        return OrderItemDto.builder()
                .id(orderItem.getId())
                .quantity(orderItem.getQuantity())
                .totalPrice(orderItem.getTotalPrice())
                .state(orderItem.getState())
                .itemDto(ItemDto.fromEntity(orderItem.getItem()))
                .orderHistoryDto(OrderHistoryDto.fromEntity(orderItem.getOrderHistory()))
                .createdTime(orderItem.getCreatedTime())
                .modifiedTime(orderItem.getModifiedTime())
                .build();
    }
}
