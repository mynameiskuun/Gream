package com.project.gream.common.enumlist.converter;

import com.project.gream.common.enumlist.PostType;

import javax.persistence.AttributeConverter;
import java.util.EnumSet;
import java.util.NoSuchElementException;

public class PostTypeConverter implements AttributeConverter<PostType, String> {
    @Override
    public String convertToDatabaseColumn(PostType attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public PostType convertToEntityAttribute(String dbData) {
        return dbData == null ? null : EnumSet.allOf(PostType.class).stream()
                .filter(v -> v.getValue().equals(dbData))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException(String.format("dbData = [$s] enum = [%s] 는 존재하지 않습니다", dbData, PostType.class)));
    }
}
