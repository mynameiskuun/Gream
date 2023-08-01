package com.project.gream.domain.member.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MemberRequestDto {

    private String id;
    private String password;
    private String name;
    private String email;
    private int point;
    private String gender;
    private String address;
    private CartDto cartDto;
    private String isOauthLogin;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdTime;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime modifiedTime;

    @Builder
    public MemberRequestDto(String id, String password, String name, String email, int point, String gender, String address,
                            CartDto cartDto, String isOauthLogin, LocalDateTime createdTime, LocalDateTime modifiedTime) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.email = email;
        this.point = point;
        this.gender = gender;
        this.address = address;
        this.cartDto = cartDto;
        this.isOauthLogin = isOauthLogin;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
    }
}
