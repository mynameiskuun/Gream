package com.project.gream.common.enumlist.converter;

import com.project.gream.common.enumlist.Grade;
import com.project.gream.common.enumlist.PostType;

import javax.persistence.AttributeConverter;
import java.util.EnumSet;
import java.util.NoSuchElementException;

public class GradeConverter implements AttributeConverter<Grade, String> {
    @Override
    public String convertToDatabaseColumn(Grade attribute) {
        return attribute == null ? null : attribute.name();
    }

    @Override
    public Grade convertToEntityAttribute(String dbData) {
        return dbData == null ? null : EnumSet.allOf(Grade.class).stream()
                .filter(v -> v.name().equals(dbData))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException(String.format("dbData = [$s] enum = [%s] 는 존재하지 않습니다", dbData, Grade.class)));
    }
}
