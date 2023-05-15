package com.project.gream.domain.member.service;

import com.project.gream.common.annotation.LoginMember;
import com.project.gream.domain.member.dto.CartItemDto;
import com.project.gream.domain.member.dto.MemberDto;
import com.project.gream.domain.member.dto.MemberRequestDto;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;


public interface MemberService {

    int userRegister(MemberDto memberDto);
    List<CartItemDto> getCartItemsByUserId(MemberDto memberDto);
    String checkQtyAndAddToCart(Map<String, Object> itemMap, MemberDto memberDto);
    String addItemToCart(Map<String, Object> itemMap, MemberDto memberDto);
    void deleteCartItemById(Long cartItemId);
    void deleteAllCartItems(Long cartId);
    ModelAndView updateAddressAndGender(MemberRequestDto req, @LoginMember MemberDto memberDto);

}
