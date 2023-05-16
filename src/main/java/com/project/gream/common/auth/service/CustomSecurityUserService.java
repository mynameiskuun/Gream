package com.project.gream.common.auth.service;

import com.project.gream.common.auth.dto.CustomUserDetails;
import com.project.gream.domain.member.entity.Member;
import com.project.gream.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class CustomSecurityUserService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new UsernameNotFoundException(String.format("userId [%s]는 없는 아이디 입니다", memberId)));
        return CustomUserDetails.builder()
                .id(member.getId())
                .password(member.getPassword())
                .role(member.getRole())
                .build();
    }
}
