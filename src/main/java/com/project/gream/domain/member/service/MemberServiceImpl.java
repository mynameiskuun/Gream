package com.project.gream.domain.member.service;

import com.project.gream.common.annotation.LoginMember;
import com.project.gream.common.enumlist.Gender;
import com.project.gream.common.enumlist.Role;
import com.project.gream.common.util.StringToEnumUtil;
import com.project.gream.domain.member.dto.CartDto;
import com.project.gream.domain.member.dto.CartItemDto;
import com.project.gream.domain.member.dto.MemberVO;
import com.project.gream.domain.member.dto.MemberRequestDto;
import com.project.gream.domain.member.entity.Member;
import com.project.gream.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final HttpSession session;
    @Override
    @Transactional
    public int userRegister(MemberVO memberVO) {

        if (memberRepository.existsById(memberVO.getId())) {
            return 1;
            // 아이디 중복
        } else if (memberRepository.existsByEmail(memberVO.getEmail())) {
            return 2;
            // 이메일 중복
        } else if (!memberRepository.existsById(memberVO.getId())) {
            memberVO.setPassword(bCryptPasswordEncoder.encode(memberVO.getPassword()));
            memberVO.setRole(Role.MEMBER);
            memberVO.setCartDto(new CartDto());
            Member member = memberVO.toEntity();
            memberRepository.save(member);
        }
        return 3; // 회원가입 성공
    }

    @Transactional
    @Override
    public void updateAddressAndGender(MemberRequestDto req, @LoginMember MemberVO memberVO) {
        Member member = memberVO.toEntity();
        Gender gender = StringToEnumUtil.getEnumFromValue(Gender.class, req.getGender());
        member.updateAddressAndGender(req.getAddress(), gender);
        memberRepository.save(member);
    }

    @Override
    public List<CartItemDto> getCartItemsByUserId(MemberVO memberVO) {
        return null;
    }

    @Override
    public String checkQtyAndAddToCart(Map<String, Object> itemMap, MemberVO memberVO) {
        return null;
    }

    @Override
    public String addItemToCart(Map<String, Object> itemMap, MemberVO memberVO) {
        return null;
    }

    @Override
    public void deleteCartItemById(Long cartItemId) {

    }

    @Override
    public void deleteAllCartItems(Long cartId) {

    }

}
