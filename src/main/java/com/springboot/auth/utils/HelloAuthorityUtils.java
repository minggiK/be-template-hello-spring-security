package com.springboot.auth.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class HelloAuthorityUtils {
    @Value("${mail.address.admin}") // $ -> Java code 로 인식
    private String adminMailAddress;

    //관리자 권한이면,  관리자+회원 권한을 모두 가지고 있을 수 있다.
    //스프링 시큐리티는 'ROLE_' 형태
    //DB가 시큐리티 환경에 종속되지 않도록 따로 관리해야한다. **기술이 앞서면 안된다 엄청
    private final List<GrantedAuthority> ADMIN_ROLES =
            AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_USER");

    private final List<GrantedAuthority> USER_ROLES =
            AuthorityUtils.createAuthorityList("ROLE_USER");

    private final List<String> ADMIN_ROLES_STRING = List.of("ADMIN", "USER");
    private final List<String> USER_ROLES_STRING = List.of("USER");

    //DB에 저장된 걸 써야하기때문이라고??? 뭐라는거야
    public List<String> createRoles(String email) {
        if(email.equals(adminMailAddress)) {
            return ADMIN_ROLES_STRING;
        }else{
            return USER_ROLES_STRING;
        }
    }

//    public List<GrantedAuthority> createAuthorities(String email) {
//        if(email.equals(adminMailAddress)) {
//            return ADMIN_ROLES;
//        } else {
//            return USER_ROLES;
//        }
//    }
    //회원가입을 할때 이메일을 넣는데 임의로 지정하는것
    //"admin@google.com" -> 이거면 무조건 이사람은 관리자야 라고 지정하는 것 // 이건 민감한 정보 , 환경변수를 외부에 불러오는것
    //환경변수를 지금까지 yml 파일에 저장해뒀음,

    public List<GrantedAuthority> createAuthorities(List<String> roles) {
        //List를 순회하며 String 을 GrantedAuthority로 바꿔서 모두 다시 List로 패키징 후 반환
        return roles.stream()
                //"ROLE_USER" / "ROLE_ADMIN" 형태로 변경해야한다
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }
}
