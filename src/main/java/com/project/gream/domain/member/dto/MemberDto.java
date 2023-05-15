package com.project.gream.domain.member.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.project.gream.common.enumlist.Gender;
import com.project.gream.common.enumlist.Role;
import com.project.gream.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MemberDto {

    private String id;
    private String password;
    private String name;
    private String address;
    private String email;
    private int point;
    private Role role;
    private Gender gender;
    private CartDto cartDto;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdTime;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime modifiedTime;


    /*
     * @AllArgsConstructor 대신 생성자와 @Builder를 사용하는 이유
     *
     * 이 어노테이션은, class 내부의 필드 순서대로 생성자의 파라미터를 만든다.
     * 하지만 이후 사용자가 임의로 필드의 순서를 변경한다면, 이미 작성해둔 인스턴스 생성 코드에서
     * 파라미터의 값이 반대로 들어가는 심각한 비지니스 로직 에러를 발생 시키기 때문이다.
     *  */

    @Builder
    public MemberDto(String id, String password, String name, String address, String email, int point,
                     Role role, Gender gender, CartDto cartDto, LocalDateTime createdTime, LocalDateTime modifiedTime) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.address = address;
        this.email = email;
        this.point = point;
        this.role = role;
        this.gender = gender;
        this.cartDto = cartDto;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
    }

    public Member toEntity() {
        return Member.builder()
                .id(id)
                .password(password)
                .name(name)
                .address(address)
                .email(email)
                .point(point)
                .role(role)
                .gender(gender)
                .cart(cartDto.toEntity())
                .build();
    }

    public static MemberDto fromEntity(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .password(member.getPassword())
                .name(member.getName())
                .address(member.getAddress())
                .email(member.getEmail())
                .point(member.getPoint())
                .role(member.getRole())
                .gender(member.getGender())
                .cartDto(CartDto.fromEntity(member.getCart()))
                .build();
    }
}
