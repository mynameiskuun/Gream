package com.project.gream.common.auth.service;

import com.project.gream.common.auth.dto.OAuthAttributes;
import com.project.gream.common.enumlist.Role;
import com.project.gream.domain.member.dto.CartDto;
import com.project.gream.domain.member.dto.MemberDto;
import com.project.gream.domain.member.entity.Cart;
import com.project.gream.domain.member.entity.Member;
import com.project.gream.domain.member.repository.MemberRepository;
import groovy.util.logging.Slf4j;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private HttpSession session;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String userNameAttributeName = userRequest
                .getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        String uuid = UUID.randomUUID().toString().substring(0, 6);
        String password = bCryptPasswordEncoder.encode(uuid);

        MemberDto memberDto = MemberDto.builder()
                .id(attributes.getEmail())
                .password(password)
                .name(attributes.getName())
                .email(attributes.getEmail())
                .role(Role.MEMBER)
                .cartDto(new CartDto())
                .build();

        memberRepository.save(memberDto.toEntity());
        session.setAttribute("loginMember", memberDto);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(Role.MEMBER.name())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

}
