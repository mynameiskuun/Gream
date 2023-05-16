package com.project.gream.domain.item.entity;


import com.project.gream.common.enumlist.Category;
import com.project.gream.common.enumlist.Gender;
import com.project.gream.common.enumlist.converter.CategoryConverter;
import com.project.gream.common.enumlist.converter.GenderConverter;
import com.project.gream.common.util.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Item extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;
    @Column(name = "item_name")
    private String name;
    @Column(name = "item_price")
    private int price;
    @Column(length = 2048, name = "item_detail")
    private String detail;
    @Column(name = "item_stock")
    private int itemStock;
    private String thumbnailUrl;
    @Column(name = "item_category")
    @Convert(converter = CategoryConverter.class)
    private Category category;
    @Convert(converter = GenderConverter.class)
    private Gender gender;

    @Builder
    public Item(Long id, String name, int price, String detail, int itemStock, String thumbnailUrl, Category category, Gender gender) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.detail = detail;
        this.itemStock = itemStock;
        this.thumbnailUrl = thumbnailUrl;
        this.category = category;
        this.gender = gender;
    }

    public void updateItemStock(int itemStock) {
        this.itemStock = itemStock;
    }

    public void updateThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

}
