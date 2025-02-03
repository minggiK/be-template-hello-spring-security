package com.springboot.auth;

import com.springboot.auth.utils.HelloAuthorityUtils;
import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.member.Member;
import com.springboot.member.MemberRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import com.springboot.auth.utils.HelloAuthorityUtils;
import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.member.Member;
import com.springboot.member.MemberRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class HelloUserDetailsServiceV2 implements UserDetailsService {
    private final MemberRepository memberRepository;
    //HelooAuthorityUtils 작성 후 -> 디비 조회해서 이메일을 가지고 권한을 만들어얗
    private final HelloAuthorityUtils authorityUtils;

    public HelloUserDetailsServiceV2(MemberRepository memberRepository, HelloAuthorityUtils authorityUtils) {
        this.memberRepository = memberRepository;
        this.authorityUtils = authorityUtils;
    }
    //DB조회

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //DB에서 불러와서 ? 뭘로? username 으로(우리의 경우 아이디는 이메일)
        Optional<Member> optionalMember = memberRepository.findByEmail(username);
        Member findMember = optionalMember.orElseThrow(()->
                new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
//        // 권한 부여하고
//        //관리자 이메일 지정해줬음
////        List<GrantedAuthority> authorities =  authorityUtils.createAuthorities(findMember.getEmail());
//        //List, Set으로 사용 할 수 있음 제네릭으로 사용 -> 왜 제네릭으로 사용해야하느지 한번 정리하기
//        Collection<? extends GrantedAuthority> authorities = authorityUtils.createAuthorities(findMember.getEmail());
//        // userDetails 형태의 객체로 만들어서 반환
//        // userDetails는 인터페이스(추상화), 구현체로 User 객체 만들거임
//        return new User(findMember.getEmail(), findMember.getPassword(), authorities);
        //-> 아래 이너클래스 : 캡슐화해서 만들어 둔 것. 위에 Collection 이 너무 많으 일을 하니 나누는 것
        // userDetails 는 해야하는 일만 남겨뒀다. : 리펙토리 했다.
        return new HelloUserDetails(findMember);
    } //-> 암호화 안함 ? 평문쓰면 안됨 -> 암호화 안해도 최소한 인코딩은 한다. // 이미 DBMemberService에서 암호 인코딩했음 !


    private final class HelloUserDetails extends Member implements UserDetails {

        public HelloUserDetails(Member member) {
            setMemberId(member.getMemberId());
            setFullName(member.getFullName());
            setEmail(member.getEmail());
            setPassword(member.getPassword());
            //member의 roles는 String 타입의 리스트를 시큐리티가 쓸수 있도록 List<GrantedAuthority> 타입으로 변경해야한다 : 반환 타입을 재 설정해야한다????
            setRoles(member.getRoles());
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return authorityUtils.createAuthorities(this.getRoles());
        }

        @Override
        public String getUsername() {
            return this.getEmail();
        }

        @Override
        public boolean isAccountNonExpired() {
            // 이 계정이 만료되지 않아야함
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
         //비밀번호가 만료되지 않아야함 : 현재는 그렇다
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }

        public HelloUserDetails(String email) {
            super(email);
        }

        public HelloUserDetails(String email, String fullName, String password) {
            super(email, fullName, password);
        }
    }


}

