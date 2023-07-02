package com.project.gream.common.enumlist.converter;

import javax.persistence.AttributeConverter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CouponDiscountForConverter implements AttributeConverter<List<String>, String> {
    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return null;
        }

        if (attribute.size() == 1) {
            return attribute.get(0);
        }

        return String.join(",", attribute);
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }

        if (!dbData.contains(",")) {
            List<String> attribute = new ArrayList<>();
            attribute.add(dbData);
            return attribute;
        } else {
            String[] array = dbData.split(",");
            return new ArrayList<>(Arrays.asList(array));
        }
    }
}
