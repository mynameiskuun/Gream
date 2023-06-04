package com.project.gream.common.enumlist.converter;

import com.project.gream.common.enumlist.CommentTargetType;
import com.project.gream.common.enumlist.Role;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.EnumSet;
import java.util.NoSuchElementException;

@Converter
public class CommentTargetTypeConverter implements AttributeConverter<CommentTargetType, String> {
    @Override
    public String convertToDatabaseColumn(CommentTargetType attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public CommentTargetType convertToEntityAttribute(String dbData) {
        return dbData == null ? null : EnumSet.allOf(CommentTargetType.class).stream()
                .filter(v -> v.getValue().equals(dbData))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException(String.format("dbData = [%s] enum = [%s] 는 존재하지 않습니다", dbData, Role.class)));
    }
}
