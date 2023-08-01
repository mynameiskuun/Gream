package com.project.gream.common.config;

import com.project.gream.common.auth.handler.CustomFailureHandler;
import com.project.gream.common.auth.handler.OAuthLoginSuccessHandler;
import com.project.gream.common.auth.handler.SecurityLoginSuccessHandler;
import com.project.gream.common.auth.service.CustomOAuth2UserService;
import com.project.gream.common.auth.service.CustomSecurityUserService;
import com.project.gream.common.enumlist.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity // 스프링 시큐리티 활성화를 위한 설정
@RequiredArgsConstructor
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final OAuthLoginSuccessHandler oAuthLoginSuccessHandler;
    private final SecurityLoginSuccessHandler securityLoginSuccessHandler;
    private final CustomFailureHandler customFailureHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSecurityUserService customSecurityUserService;
    private final PasswordEncoder bCryptEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
//                .cors().disable()
//                .csrf().disable()
                .headers().addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)).frameOptions().disable(); // h2DB 사용 위하여

        http
                .authorizeRequests()
                .antMatchers("/h2-console/**", "/", "/lib/**", "/item/**", "/login", "/join", "/test/**", "/profile", "/error", "/post/notice/", "/post/notice/**").permitAll()
                .antMatchers("/admin/**").hasRole(Role.ADMIN.name())
                .antMatchers("/member/**", "/mypage", "/mypage/**", "/review", "/review/**", "/cart", "/cart/**", "/order","/order/**").hasRole(Role.MEMBER.name())
                .anyRequest().authenticated();

        http
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/formLogin")
                .successHandler(securityLoginSuccessHandler)
                .failureHandler(customFailureHandler)
                .usernameParameter("memId")
                .passwordParameter("memPw")
                .permitAll();

        http
                .oauth2Login()
                .loginPage("/login")
//                        .userInfoEndpoint() // 로그인 성공 후 사용자 정보를 가져온다
//                        .userService(customOAuth2UserService2); // userInfoEndpoint로 가져온 사용자 정보를 처리할 때 사용한다
                .defaultSuccessUrl("/")
                .successHandler(oAuthLoginSuccessHandler)
                .failureHandler(customFailureHandler)
                .userInfoEndpoint()
                .userService(customOAuth2UserService);

        http
                .logout()
                .invalidateHttpSession(true).deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication().withUser("user").password("{noop}1111").roles("MEMBER", "ADMIN");
        auth.userDetailsService(customSecurityUserService).passwordEncoder(bCryptEncoder);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations()); // resources 이하 파일 권한 없이 접근 가능
        web.ignoring().requestMatchers(new AntPathRequestMatcher("/fonts/**")); // font는 기본 설정 외 사항 이므로 별도 설정
    }

}
