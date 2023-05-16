package com.project.gream.common.enumlist.converter;

import com.project.gream.common.enumlist.Role;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.EnumSet;
import java.util.NoSuchElementException;

@Converter
public class RoleConverter implements AttributeConverter<Role, String>{
    @Override
    public String convertToDatabaseColumn(Role attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public Role convertToEntityAttribute(String dbData) {
        return dbData == null ? null : EnumSet.allOf(Role.class).stream()
                .filter(v -> v.getValue().equals(dbData))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException(String.format("dbData = [%s] enum = [%s] 는 존재하지 않습니다", dbData, Role.class)));
    }
}
