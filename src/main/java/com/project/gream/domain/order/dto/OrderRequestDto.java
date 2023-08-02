package com.project.gream.domain.order.dto;

import com.project.gream.common.enumlist.OrderState;
import com.project.gream.domain.item.dto.ItemDto;
import com.project.gream.domain.member.dto.CartItemDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class OrderRequestDto {

    private List<CartItemDto> cartItemDtoList;
    private ItemDto itemDto;
    private int quantity;
    private Long orderItemId;
    private String orderStatus;

}
