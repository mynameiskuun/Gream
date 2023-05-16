package com.project.gream.common.auth.handler;

import com.project.gream.domain.member.dto.MemberVO;
import com.project.gream.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
public class SecurityLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final MemberRepository memberRepository;
    private final HttpSession session;
    private RequestCache requestCache = new HttpSessionRequestCache();
    private RedirectStrategy redirectStratgy = new DefaultRedirectStrategy();
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        String memberId = authentication.getName();
        MemberVO memberVO = MemberVO.fromEntity(memberRepository.findById(memberId)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("<SuccessHandler> userId [%s] 는 존재하지 않습니다", memberId))));

        session.setAttribute("loginMember", memberVO);
        resultRedirectStrategy(request, response);
    }

    protected void resultRedirectStrategy(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        SavedRequest savedRequest = requestCache.getRequest(request, response);

        String targetUrl = savedRequest == null ? "/" : savedRequest.getRedirectUrl();
        redirectStratgy.sendRedirect(request, response, targetUrl);

//        if(savedRequest != null) {
//            String targetUrl = savedRequest.getRedirectUrl();
//            redirectStratgy.sendRedirect(request, response, targetUrl); // targetUrl이 있다면 해당 Url로 이동
//        } else {
//            redirectStratgy.sendRedirect(request, response, "/"); // 없다면 default 메인화면으로
//        }

    }
}
