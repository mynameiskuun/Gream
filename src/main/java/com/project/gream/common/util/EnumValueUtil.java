package com.project.gream.common.util;

import com.project.gream.common.enumlist.EnumBase;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.EnumSet;
import java.util.NoSuchElementException;
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EnumValueUtil {
        public static <T extends Enum<T> & EnumBase> String toDbCode(T enumClass) {
            return enumClass == null ? null : enumClass.name();
        }

        public static <T extends Enum<T> & EnumBase> T toEntityCode(Class<T> enumClass, String dbCode) {
            return dbCode == null ? null :
                    EnumSet.allOf(enumClass).stream()
                            .filter(c -> c.name().equals(dbCode))
                            .findAny()
                            .orElseThrow(() -> new NoSuchElementException(String.format("enum=[%s], code=[%s]는 존재하지 않습니다.", enumClass.getName(), dbCode)));
        }
}