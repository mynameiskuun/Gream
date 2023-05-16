package com.project.gream.common.util;

import com.project.gream.common.enumlist.EnumBase;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.NoSuchElementException;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

    @CreatedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(updatable = false)
    private LocalDateTime createdTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @LastModifiedDate
    private LocalDateTime modifiedTime;

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class EnumValueUtil {

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
}
