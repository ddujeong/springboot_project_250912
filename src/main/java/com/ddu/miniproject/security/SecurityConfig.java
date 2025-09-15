package com.ddu.miniproject.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			// 모든 요청에 대해 인증 없이 접근 허용
			.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
					.requestMatchers("/**").permitAll())
			.formLogin((formLogin)-> formLogin // 스프링 시큐리티에서 로그인 설정
					.loginPage("/member/login") // 로그인 요청
					.usernameParameter("memberid")
				    .passwordParameter("memberpw")
					.defaultSuccessUrl("/")) // 로그인 성공 시 이동할 페이지 ("/" -> 루트)로 지정
					.logout((logout) -> logout
							.logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
							.logoutSuccessUrl("/") // 로그아웃 성공 시 이동할 페이지 
							.invalidateHttpSession(true)) // 세션 삭제
			;
		return http.build();
	}
	
	@Bean // 비밀번호 암호화
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean // 스프링 시큐리티에서 인증을 처리하는 매니저 클래스
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
	throws Exception{
		return authenticationConfiguration.getAuthenticationManager();
	}
}
