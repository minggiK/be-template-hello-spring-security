package com.springboot.member;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//@Service  // @Component사용해도 됨
public class InMemoryMemberService implements MemberService {
  //DB를 주입하는 객체
    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    public InMemoryMemberService(UserDetailsManager userDetailsManager, PasswordEncoder passwordEncoder) {
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Member createMember(Member member) {
        //GrantedAuthority(인터페이스) 객체로 가지고 있어야해
        //우리가 사용하는 형태 또는 커스텀 마이징 할 수 있기 때문에 추상화(인터페이스) 형태로 가지고 있는 것
        //인증방법을 회원형태에따라 구현을 다르게할 수 있기 때문에 추상화되어있다.
        //한명의 유저가 여러개의 role(권한)을 가지고 있을 있어서  List 형태로 받음
        List<GrantedAuthority> authorities = createAuthorities(Member.MemberRole.ROLE_USER.name());

        String encryptedPassword = passwordEncoder.encode(member.getPassword());

        //씨큐리티에서 유저의 특징, pw 인코딩한 것, 권한을 받아서 //-> 씨큐리티에서 내부적으로 사용하는 것
        //씨큐리티 인증 프로세스에서 유저인증정보를 가지고있음 -> 보관될때 이 형태로 보관됨
        UserDetails userDetails = new User(member.getEmail(), encryptedPassword, authorities);

    userDetailsManager.createUser(userDetails);

    return member;

    }
    //한명의 유저가 한개의 role을 가지고 있지 않아
    //admin은 유저의 마이페이지를 볼 수 있음 한명의 여러개의 형태를 받을 수 있으니 LIST로 받는 것
    private List<GrantedAuthority> createAuthorities(String... roles) {
        return Arrays.stream(roles)
                //GrantedAuthority의 구현체 SimpleGrantedAuthority
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());
    }
}
