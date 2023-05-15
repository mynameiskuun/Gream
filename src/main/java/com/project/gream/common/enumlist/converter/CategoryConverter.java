package com.project.gream.common.enumlist.converter;

import com.project.gream.common.enumlist.Category;

import javax.persistence.Converter;

@Converter
public class CategoryConverter extends EnumAttributeConverter<Category>{

    public CategoryConverter() {
        super(Category.class);}
}
