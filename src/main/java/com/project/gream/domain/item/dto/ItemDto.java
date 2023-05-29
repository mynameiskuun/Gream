package com.project.gream.domain.item.dto;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.project.gream.common.enumlist.Category;
import com.project.gream.common.enumlist.Gender;
import com.project.gream.common.util.StringToEnumUtil;
import com.project.gream.domain.item.entity.Item;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemDto {

    private Long id;
    private Category category;
    private Gender gender;
    private String name;
    private int price;
    private String detail;
    private int itemStock;
    private String thumbnailUrl;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdTime;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime modifiedTime;

    @Builder
    public ItemDto(Long id, Category category, Gender gender, String name, int price, String detail, int itemStock,
                   String thumbnailUrl, LocalDateTime createdTime, LocalDateTime modifiedTime) {
        this.id = id;
        this.category = category;
        this.gender = gender;
        this.name = name;
        this.price = price;
        this.detail = detail;
        this.itemStock = itemStock;
        this.thumbnailUrl = thumbnailUrl;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
    }

    public ItemDto(ItemRequestDto itemRequestDto) {
        this.id = itemRequestDto.getId();
        this.category = StringToEnumUtil.getEnumFromValue(Category.class, itemRequestDto.getCategory());
        this.gender = StringToEnumUtil.getEnumFromValue(Gender.class, itemRequestDto.getGender());
        this.name = itemRequestDto.getName();
        this.price = itemRequestDto.getPrice();
        this.detail = itemRequestDto.getDetail();
        this.itemStock = itemRequestDto.getItemStock();
        this.thumbnailUrl = itemRequestDto.getThumbnailUrl();
        this.createdTime = itemRequestDto.getCreatedTime();
        this.modifiedTime = itemRequestDto.getModifiedTime();
    }

    public Item toEntity() {
        return Item.builder()
                .id(id)
                .category(category)
                .gender(gender)
                .price(price)
                .name(name)
                .detail(detail)
                .itemStock(itemStock)
                .thumbnailUrl(thumbnailUrl)
                .build();
    }

    public static ItemDto fromEntity(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .category(item.getCategory())
                .gender(item.getGender())
                .name(item.getName())
                .price(item.getPrice())
                .detail(item.getDetail())
                .itemStock(item.getItemStock())
                .thumbnailUrl(item.getThumbnailUrl())
                .createdTime(item.getCreatedTime())
                .modifiedTime(item.getModifiedTime())
                .build();
    }

}
