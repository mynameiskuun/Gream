package com.project.gream.common.enumlist.converter;

import com.project.gream.common.enumlist.EnumBase;
import com.project.gream.common.util.EnumValueUtil;
import lombok.Getter;

import javax.persistence.AttributeConverter;

@Getter
public class EnumAttributeConverter<T extends Enum<T> & EnumBase> implements AttributeConverter<T, String> {

    private Class<T> enumClass;
    public EnumAttributeConverter(Class<T> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public String convertToDatabaseColumn(T attribute) {
        return attribute == null ? null : EnumValueUtil.toDbCode(attribute);
    }

    @Override
    public T convertToEntityAttribute(String dbData) {
        return dbData == null ? null : EnumValueUtil.toEntityCode(enumClass, dbData);
    }
}
