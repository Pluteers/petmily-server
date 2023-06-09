package com.pet.petmily.user.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.pet.petmily.user.repository.MemberRepository;
import com.pet.petmily.user.filter.JwtAuthenticationProcessingFilter;
import com.pet.petmily.user.service.JwtService;
import com.pet.petmily.user.filter.CustomJsonUsernamePasswordAuthenticationFilter;
import com.pet.petmily.user.handler.LoginSuccessHandler;
import com.pet.petmily.user.handler.LoginFailureHandler;
import com.pet.petmily.user.service.LoginService;

//소셜 로그인 패키지
import com.pet.petmily.user.oauth2.handler.OAuth2LoginFailureHandler;
import com.pet.petmily.user.oauth2.handler.OAuth2LoginSuccessHandler;
import com.pet.petmily.user.oauth2.service.CustomOAuth2UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

/**
 * 인증은 CustomJsonUsernamePasswordAuthenticationFilter에서 authenticate()로 인증된 사용자로 처리
 * JwtAuthenticationProcessingFilter는 AccessToken, RefreshToken 재발급
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final LoginService loginService;
    private final JwtService jwtService;
    private final MemberRepository memberRepository;
    private final ObjectMapper objectMapper;

    //OAuth부분
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final CustomOAuth2UserService customOAuth2UserService;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .formLogin().disable() // FormLogin 사용 X
                .httpBasic().disable() // httpBasic 사용 X
                .csrf().disable() // csrf 보안 사용 X
                .headers().frameOptions().disable()
                .and()

                // 세션 사용하지 않으므로 STATELESS로 설정
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //== URL별 권한 관리 옵션 ==//
                .authorizeRequests()
                // 아이콘, css, js 관련
                // 기본 페이지, css, image, js 하위 폴더에 있는 자료들은 모두 접근 가능, h2-console에 접근 가능
                .antMatchers("/","/css/**","/images/**","/js/**","/favicon.ico","/h2-console/**").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN") // admin으로 시작하는 경로는 ADMIN 권한만 접근 가능
                .antMatchers("/user/**").hasAnyRole("USER","ADMIN") // user으로 시작하는 경로는 USER,ADMIN 권한만 접근 가능
                .antMatchers("/index", "/login").permitAll() // index, login 페이지는 모두 접근 가능
                .antMatchers("/sign-up/**").permitAll() // sign-up 페이지는 모두 접근 가능
                .antMatchers("/oauth/**").permitAll() // api로 시작하는 경로는 모두 접근 가능
                .antMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**").permitAll()



                .anyRequest().authenticated()// 위의 경로 이외에는 모두 인증된 사용자만 접근 가능


                .and()
                //== 소셜 로그인 설정 ==//
                .oauth2Login()


                .successHandler(oAuth2LoginSuccessHandler) // 동의하고 계속하기를 눌렀을 때 Handler 설정
                .failureHandler(oAuth2LoginFailureHandler) // 소셜 로그인 실패 시 핸들러 설정
                .userInfoEndpoint().userService(customOAuth2UserService);




        // 원래 스프링 시큐리티 필터 순서가 LogoutFilter 이후에 로그인 필터 동작
        // 따라서, LogoutFilter 이후에 우리가 만든 필터 동작하도록 설정
        // 순서 : LogoutFilter -> JwtAuthenticationProcessingFilter -> CustomJsonUsernamePasswordAuthenticationFilter
        http.addFilterAfter(customJsonUsernamePasswordAuthenticationFilter(), LogoutFilter.class);

        http.addFilterBefore(jwtAuthenticationProcessingFilter(), CustomJsonUsernamePasswordAuthenticationFilter.class);


        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {

        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * AuthenticationManager 설정 후 등록
     * PasswordEncoder를 사용하는 AuthenticationProvider 지정 (PasswordEncoder는 위에서 등록한 PasswordEncoder 사용)
     * FormLogin(기존 스프링 시큐리티 로그인)과 동일하게 DaoAuthenticationProvider 사용
     * UserDetailsService는 커스텀 LoginService로 등록
     * 또한, FormLogin과 동일하게 AuthenticationManager로는 구현체인 ProviderManager 사용(return ProviderManager)
     *
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        //DaoAuthenticationProvider를 사용하여 AuthenticationManager를 생성하여 반환
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(loginService);
        //Provider에서 이전에 설정한 PasswordEncoder를 설정하고,
        //이전에 만들었던 UserDetails 유저를 반환하는 LoginService를 설정한 후 반환
        return new ProviderManager(provider);
    }

    /**
     * 로그인 성공 시 호출되는 LoginSuccessJWTProviderHandler 빈 등록
     */
    @Bean
    public LoginSuccessHandler loginSuccessHandler() {


        return new LoginSuccessHandler(jwtService, memberRepository);
    }

    /**
     * 로그인 실패 시 호출되는 LoginFailureHandler 빈 등록
     */
    @Bean
    public LoginFailureHandler loginFailureHandler() {

        return new LoginFailureHandler();
    }

    /**
     * CustomJsonUsernamePasswordAuthenticationFilter 빈 등록
     * 커스텀 필터를 사용하기 위해 만든 커스텀 필터를 Bean으로 등록
     * setAuthenticationManager(authenticationManager())로 위에서 등록한 AuthenticationManager(ProviderManager) 설정
     * 로그인 성공 시 호출할 handler, 실패 시 호출할 handler로 위에서 등록한 handler 설정
     */
    @Bean
    public CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordAuthenticationFilter() {
        CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordLoginFilter
                = new CustomJsonUsernamePasswordAuthenticationFilter(objectMapper);
        customJsonUsernamePasswordLoginFilter.setAuthenticationManager(authenticationManager());
        customJsonUsernamePasswordLoginFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
        customJsonUsernamePasswordLoginFilter.setAuthenticationFailureHandler(loginFailureHandler());
        return customJsonUsernamePasswordLoginFilter;
    }

    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
        JwtAuthenticationProcessingFilter jwtAuthenticationFilter = new JwtAuthenticationProcessingFilter(jwtService, memberRepository);

        return jwtAuthenticationFilter;
    }
}