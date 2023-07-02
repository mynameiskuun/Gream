package com.project.gream.common.enumlist.converter;

import com.project.gream.common.enumlist.CouponStatus;

import javax.persistence.AttributeConverter;
import java.util.EnumSet;
import java.util.NoSuchElementException;

public class CouponStatusConverter implements AttributeConverter<CouponStatus, String> {


    @Override
    public String convertToDatabaseColumn(CouponStatus attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public CouponStatus convertToEntityAttribute(String dbData) {
        return dbData == null ? null : EnumSet.allOf(CouponStatus.class).stream()
                .filter(v -> v.getValue().equals(dbData))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException(String.format("dbData = [$s] enum = [%s] 는 존재하지 않습니다", dbData, CouponStatus.class)));
    }
}
