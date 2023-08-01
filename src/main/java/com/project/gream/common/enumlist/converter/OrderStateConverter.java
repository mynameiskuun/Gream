package com.project.gream.common.enumlist.converter;

import com.project.gream.common.enumlist.CouponStatus;
import com.project.gream.common.enumlist.OrderState;

import javax.persistence.AttributeConverter;
import java.util.EnumSet;
import java.util.NoSuchElementException;

public class OrderStateConverter implements AttributeConverter<OrderState, String> {


    @Override
    public String convertToDatabaseColumn(OrderState attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public OrderState convertToEntityAttribute(String dbData) {
        return dbData == null ? null : EnumSet.allOf(OrderState.class).stream()
                .filter(v -> v.getValue().equals(dbData))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException(String.format("dbData = [$s] enum = [%s] 는 존재하지 않습니다", dbData, CouponStatus.class)));
    }
}
