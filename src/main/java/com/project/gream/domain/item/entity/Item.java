package com.project.gream.domain.item.entity;


import com.project.gream.common.enumlist.Category;
import com.project.gream.common.enumlist.Gender;
import com.project.gream.common.enumlist.converter.CategoryConverter;
import com.project.gream.common.enumlist.converter.GenderConverter;
import com.project.gream.domain.member.entity.BaseTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Item extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int price;
    @Column(length = 2048, nullable = false)
    private String detail;
    private int itemStock;
    private String thumbNailUrl;
    @Convert(converter = CategoryConverter.class)
    private Category category;
    @Convert(converter = GenderConverter.class)
    private Gender gender;

}
