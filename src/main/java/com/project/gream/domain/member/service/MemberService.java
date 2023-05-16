package com.project.gream.domain.member.service;

import com.project.gream.common.annotation.LoginMember;
import com.project.gream.domain.member.dto.CartItemDto;
import com.project.gream.domain.member.dto.MemberVO;
import com.project.gream.domain.member.dto.MemberRequestDto;

import java.util.List;
import java.util.Map;


public interface MemberService {

    int userRegister(MemberVO memberVO);
    List<CartItemDto> getCartItemsByUserId(MemberVO memberVO);
    String checkQtyAndAddToCart(Map<String, Object> itemMap, MemberVO memberVO);
    String addItemToCart(Map<String, Object> itemMap, MemberVO memberVO);
    void deleteCartItemById(Long cartItemId);
    void deleteAllCartItems(Long cartId);
    void updateAddressAndGender(MemberRequestDto req, @LoginMember MemberVO memberVO);

}
