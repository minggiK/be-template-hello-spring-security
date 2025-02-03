//package com.springboot.auth;
//
//import com.springboot.auth.utils.HelloAuthorityUtils;
//import com.springboot.exception.BusinessLogicException;
//import com.springboot.exception.ExceptionCode;
//import com.springboot.member.Member;
//import com.springboot.member.MemberRepository;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Component;
//
//import java.util.Collection;
//import java.util.List;
//import java.util.Optional;
//
//@Component
//public class HelloUserDetailsServiceV1 implements UserDetailsService {
//    private final MemberRepository memberRepository;
//    //HelooAuthorityUtils 작성 후 -> 디비 조회해서 이메일을 가지고 권한을 만들어얗
//    private final HelloAuthorityUtils authorityUtils;
//
//    public HelloUserDetailsServiceV1(MemberRepository memberRepository, HelloAuthorityUtils authorityUtils) {
//        this.memberRepository = memberRepository;
//        this.authorityUtils = authorityUtils;
//    }
//    //DB조회
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//       //DB에서 불러와서 ? 뭘로? username 으로(우리의 경우 아이디는 이메일)
//        Optional<Member> optionalMember = memberRepository.findByEmail(username);
//        Member findMember = optionalMember.orElseThrow(()->
//                new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
//        // 권한 부여하고
//        //관리자 이메일 지정해줬음
////        List<GrantedAuthority> authorities =  authorityUtils.createAuthorities(findMember.getEmail());
//        //List, Set으로 사용 할 수 있음 제네릭으로 사용 -> 왜 제네릭으로 사용해야하느지 한번 정리하기
//        Collection<? extends GrantedAuthority> authorities = authorityUtils.createAuthorities(findMember.getEmail());
//        // userDetails 형태의 객체로 만들어서 반환
//        // userDetails는 인터페이스(추상화), 구현체로 User 객체 만들거임
//        return new User(findMember.getEmail(), findMember.getPassword(), authorities);
//    } //-> 암호화 안함 ? 평문쓰면 안됨 -> 암호화 안해도 최소한 인코딩은 한다. // 이미 DBMemberService에서 암호 인코딩했음 !
//}
