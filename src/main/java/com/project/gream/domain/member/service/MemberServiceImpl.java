package com.project.gream.domain.member.service;

import com.project.gream.common.annotation.LoginMember;
import com.project.gream.common.enumlist.Gender;
import com.project.gream.common.enumlist.Role;
import com.project.gream.common.util.StringToEnumUtil;
import com.project.gream.domain.member.dto.CartItemDto;
import com.project.gream.domain.member.dto.MemberDto;
import com.project.gream.domain.member.dto.MemberRequestDto;
import com.project.gream.domain.member.entity.Cart;
import com.project.gream.domain.member.entity.Member;
import com.project.gream.domain.member.repository.CartRepository;
import com.project.gream.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

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
    public int userRegister(MemberDto memberDto) {

        if (memberRepository.existsById(memberDto.getId())) {
            return 1;
            // 아이디 중복
        } else if (memberRepository.existsByEmail(memberDto.getEmail())) {
            return 2;
            // 이메일 중복
        } else if (!memberRepository.existsById(memberDto.getId())) {
            memberDto.setPassword(bCryptPasswordEncoder.encode(memberDto.getPassword()));
            memberDto.setRole(Role.MEMBER);
            Member member = memberDto.toEntity();
            member.setCart(new Cart());
            memberRepository.save(member);
        }
        return 3; // 회원가입 성공
    }

    @Transactional
    @Override
    public ModelAndView updateAddressAndGender(MemberRequestDto req, @LoginMember MemberDto memberDto) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/");
        Member member = memberDto.toEntity();
        Gender gender = StringToEnumUtil.getEnumFromValue(Gender.class, req.getGender());
        member.updateAddressAndGender(req.getAddress(), gender);
        memberRepository.save(member);
        return mav;
    }

    @Override
    public List<CartItemDto> getCartItemsByUserId(MemberDto memberDto) {
        return null;
    }

    @Override
    public String checkQtyAndAddToCart(Map<String, Object> itemMap, MemberDto memberDto) {
        return null;
    }

    @Override
    public String addItemToCart(Map<String, Object> itemMap, MemberDto memberDto) {
        return null;
    }

    @Override
    public void deleteCartItemById(Long cartItemId) {

    }

    @Override
    public void deleteAllCartItems(Long cartId) {

    }

}
