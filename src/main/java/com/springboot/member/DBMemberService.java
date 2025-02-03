package com.springboot.member;

import com.springboot.auth.utils.HelloAuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DBMemberService implements MemberService {
    private final PasswordEncoder passwordEncoder;
    //DB에 저장해야하니까!
    private final MemberRepository memberRepository;
    private final HelloAuthorityUtils authorityUtils;

    public DBMemberService(PasswordEncoder passwordEncoder, MemberRepository memberRepository, HelloAuthorityUtils authorityUtils) {
        this.passwordEncoder = passwordEncoder;
        this.memberRepository = memberRepository;
        this.authorityUtils = authorityUtils;
    }

    @Override
    public Member createMember(Member member) {

//        verifiedExistsEmail(member.getEmail());
        //pw 암호화
        String encryptedPassword = passwordEncoder
                .encode(member.getPassword());
        member.setPassword(encryptedPassword);

        // 이걸 하려고 만들었는데 왜 만들었는지 모르겠고 지금 뭐하는지도 모르겠음
        List<String> roles = authorityUtils.createRoles(member.getEmail());
        member.setRoles(roles);


        Member saveMember = memberRepository.save(member);
        return saveMember;

        //DB에 저장한 내용을 userDetails 로 변경해야해 -> db를 쓸때는
    }
}
