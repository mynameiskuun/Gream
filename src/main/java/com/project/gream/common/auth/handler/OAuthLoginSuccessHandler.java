package com.project.gream.common.auth.handler;

import com.project.gream.domain.member.dto.MemberVO;
import com.project.gream.domain.member.entity.Member;
import com.project.gream.domain.member.repository.MemberRepository;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuthLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final MemberRepository memberRepository;
    private final HttpSession session;
    private RequestCache requestCache = new HttpSessionRequestCache();
    private RedirectStrategy redirectStratgy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
        String email = principal.getAttribute("email");

        resultRedirectStrategy(request, response, email);

    }

    public void resultRedirectStrategy(HttpServletRequest request, HttpServletResponse response, String email) throws IOException, ServletException {

        Member member = memberRepository.findById(email).orElseThrow();
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        String targetUrl;

        if (checkBothNotNull(member)) {
            session.setAttribute("loginMember", MemberVO.fromEntity(member));
            targetUrl = savedRequest == null ? "/" : savedRequest.getRedirectUrl();
        } else
            targetUrl = "/address";
        redirectStratgy.sendRedirect(request, response, targetUrl);
    }
    private boolean checkBothNotNull(Member member) {
        return member.getAddress() != null && member.getGender() != null;
    }
}
