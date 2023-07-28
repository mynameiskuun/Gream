package com.project.gream.domain.member.entity;

import com.project.gream.common.enumlist.Gender;
import com.project.gream.common.enumlist.Grade;
import com.project.gream.common.enumlist.Role;
import com.project.gream.common.enumlist.converter.GenderConverter;
import com.project.gream.common.enumlist.converter.GradeConverter;
import com.project.gream.common.enumlist.converter.RoleConverter;
import com.project.gream.common.util.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@DynamicUpdate
@DynamicInsert
@NoArgsConstructor
@Getter
@Entity
public class Member extends BaseTimeEntity {

    @Id
    @Column(name = "member_id")
    private String id;
    @Column(name = "member_password")
    private String password;
    @Column(name = "member_name")
    private String name;
    @Column(name = "member_address")
    private String address;
    @Column(name = "member_email")
    private String email;
    @Column(columnDefinition = "integer default 0", name = "member_point")
    private int point;
    @Convert(converter = RoleConverter.class)
    @Column(name = "role")
    private Role role;
    @Convert(converter = GenderConverter.class)
    @Column(name = "member_gender")
    private Gender gender;
//    @Convert(converter = GradeConverter.class)
//    @Column(name = "member_grade")
//    private Grade grade;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @Builder
    public Member(String id, String password, String name, String address, String email,
                  int point, Role role, Gender gender, Grade grade, Cart cart) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.address = address;
        this.email = email;
        this.point = point;
        this.role = role;
        this.gender = gender;
//        this.grade = grade;
        this.cart = cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public void updateAddressAndGender(String address, Gender gender) {
        this.address = address;
        this.gender = gender;
    }
    public void updatePassword(String password) {
        this.password = password;
    }
    public void updateAddress(String address) {
        this.address = address;
    }
    public void updateEmail(String email) {
        this.email = email;
    }
    public void resetPointAfterOrder(int point, int reward) {
        if (this.point < point) {
            return;
        }
        this.point -= point;
        this.point += reward;
    }

    public void addPoint(int point) {
        this.point += point;
    }
}
