package com.project.gream.common.enumlist.converter;

import com.project.gream.common.enumlist.QnaType;

import javax.persistence.AttributeConverter;
import java.util.EnumSet;
import java.util.NoSuchElementException;

public class QnaTypeConverter implements AttributeConverter<QnaType, String> {
    @Override
    public String convertToDatabaseColumn(QnaType attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public QnaType convertToEntityAttribute(String dbData) {
        return dbData == null ? null : EnumSet.allOf(QnaType.class).stream()
                .filter(v -> v.getValue().equals(dbData))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException(String.format("dbData = [$s] enum = [%s] 는 존재하지 않습니다", dbData, QnaType.class)));
    }
}
