package com.project.gream.domain.order.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.project.gream.domain.member.dto.MemberDto;
import com.project.gream.domain.order.entity.OrderHistory;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter
public class OrderHistoryDto {

    private Long id;
    private MemberDto memberDto;
    private int usePoint;
    private Long totalOrderPrice;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
//    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdTime;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
//    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime modifiedTime;


    @Builder
    public OrderHistoryDto(Long id, MemberDto memberDto, int usePoint, Long totalOrderPrice,
                           LocalDateTime createdTime, LocalDateTime modifiedTime) {
        this.id = id;
        this.memberDto = memberDto;
        this.usePoint = usePoint;
        this.totalOrderPrice = totalOrderPrice;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
    }

    public OrderHistory toEntity() {
        return OrderHistory.builder()
                .id(id)
                .member(memberDto.toEntity())
                .usePoint(usePoint)
                .totalOrderPrice(totalOrderPrice)
                .build();
    }

    public static OrderHistoryDto fromEntity(OrderHistory orderHistory) {
        return OrderHistoryDto.builder()
                .id(orderHistory.getId())
                .memberDto(MemberDto.fromEntity(orderHistory.getMember()))
                .usePoint(orderHistory.getUsePoint())
                .totalOrderPrice(orderHistory.getTotalOrderPrice())
                .createdTime(orderHistory.getCreatedTime())
                .modifiedTime(orderHistory.getModifiedTime())
                .build();
    }

}
