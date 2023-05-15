package com.project.gream.common.enumlist.converter;

import com.project.gream.common.enumlist.Gender;

import javax.persistence.Converter;

@Converter
public class GenderConverter extends EnumAttributeConverter<Gender> {
    public GenderConverter() {
        super(Gender.class);
    }
}
