package com.project.gream.common.enumlist.converter;

import com.project.gream.common.enumlist.PointRate;

import javax.persistence.Converter;

@Converter
public class PointRateConverter extends EnumAttributeConverter<PointRate> {
    public PointRateConverter() {super(PointRate.class);}
}
