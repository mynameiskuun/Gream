package com.project.gream.common.util;
import com.project.gream.common.enumlist.EnumBase;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.util.EnumSet;
import java.util.NoSuchElementException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StringToEnumUtil {
    public static <T extends Enum<T> & EnumBase> T getEnumFromValue(Class<T> enumClass, String value) {

        return value == null ? null :
                EnumSet.allOf(enumClass).stream()
                        .filter(e -> e.name().equals(value.toUpperCase()))
                        .findAny()
                        .orElseThrow(() -> new NoSuchElementException(String.format("value[%s] enum[%s]는 존재하지 않습니다", value, enumClass)));
    }
}