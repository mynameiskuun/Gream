package com.project.gream.domain.member.entity;

import com.project.gream.common.enumlist.Gender;
import com.project.gream.common.enumlist.Role;
import com.project.gream.common.enumlist.converter.GenderConverter;
import com.project.gream.common.enumlist.converter.RoleConverter;
import com.project.gream.common.util.BaseTimeEntity;
import com.project.gream.domain.coupon.CouponBox;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

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
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "cart_id")
    private Cart cart;
//    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
//    @JoinColumn(name = "couponbox_id")
//    private CouponBox couponBox;

    @Builder
    public Member(String id, String password, String name, String address, String email,
                  int point, Role role, Gender gender, Cart cart, CouponBox couponBox) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.address = address;
        this.email = email;
        this.point = point;
        this.role = role;
        this.gender = gender;
        this.cart = cart;
//        this.couponBox = couponBox;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public void updateAddressAndGender(String address, Gender gender) {
        this.address = address;
        this.gender = gender;
    }
}
