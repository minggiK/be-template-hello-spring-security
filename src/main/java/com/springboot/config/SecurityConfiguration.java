package com.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {
    //보안 구성 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http//origin이 달라서 h2 접속이 안됨 그래서 설정해주는 거
                .headers().frameOptions().sameOrigin()
                .and()
                //csrf 끊은거야 : 안끄면 몇 페이지 안나옴
                .csrf().disable()
                //form 로그인 방식으로 진행할거야
                .formLogin()
                //로그인 페이지를 만들거야 : 씨큐리티가 만들어놓은거 쓰는거 아니고
                //왜 login.html<> 로 이동하는가? 브라우저에서 이 uri로 이동하는거 //auth - AuthController에서 get 요청이 날아간다? 왜?
                // 껏다 켜면 다 날아감, 실제로 이렇게 사용ㅌ -> Security가 인증과정을 대신하는 것
                .loginPage("/auths/login-form")
                //로그인을 수행할 페이지 지정
                    //로그인을 실제로 진행하는 과정
                .loginProcessingUrl("/process_login")
                //로그인이 실패했을 떄 페이지 이동  /파라미터로 에러를 전달
                .failureUrl("/auths/login-form?error")
                .and()
//                로그아웃 구현 : Security가 권한을 갖고있게 두면 안된다
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .and()
                //..권한이 없는 페이지로 이동했을때 access-denied.html로 이동
                .exceptionHandling().accessDeniedPage("/auths/access-denied")
                .and()
                //2번째 : 관리자만 접근할 수 있어요. -> user 라는 Id를 갖고있는 사람만 가능하다고 설정한 것
                //순서 지켜 : 코드는 위에서 아래로 흘러 /**이 위에 있으면 그 아래 코드는 실행되지 않아
                .authorizeHttpRequests(authorize -> authorize
                        .antMatchers("/orders/**").hasRole("ADMIN")
                        .antMatchers("/members/my-page").hasRole("USER")
                        .antMatchers("/**").permitAll());
              //인가 부분 : 요청에 따라 나눌건데
//                .authorizeHttpRequests()
                ///roles에 상관없이 다 요청 보낼거야
//                .anyRequest()
//                .permitAll();

        return http.build();
    }

    //회원 생성 코드
//    @Bean
//    public UserDetailsManager userDetailsManager(){
//       //씨큐리티에서 관리하는 user 정보를 반환
//        // UserDetails(인증된 유저) 의 구현체 User를 사용
//            //withDefaultPasswordEncoder는 사용하지 않음(테스트니까 사용)
//        UserDetails userDetails = User.withDefaultPasswordEncoder()
//                .username("ming@naver.com")
//                .password("0090")
//                .roles("USER")
//                .build(); //-> 전체주문 목록 보기는 접근 안되야 함
//
//        UserDetails admin = User.withDefaultPasswordEncoder()
//                .username("admin@naver.com")
//                .password("0099")
//                .roles("ADMIN")
//                .build(); // -> 마이페이지는 안나와야해요
//
//        //user를 인메모리에서 관리
//        //Security 구현할때 PW는 암호화가 기본이다. -> PW인코더를 만들어야함
//       return new InMemoryUserDetailsManager(userDetails, admin);
//    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        //PasswordEncoder를 관리하는 객체가 여러개가 있다.
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
