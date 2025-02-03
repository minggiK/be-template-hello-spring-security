package com.springboot.member;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;
    private final MemberMapper mapper;

    public MemberController(MemberService memberService, MemberMapper mapper) {
        this.memberService = memberService;
        this.mapper = mapper;
    }

    @GetMapping("/register")
    public String registerMemberForm() {
        return "member-register";
    }

    @PostMapping("/register")
    public String registerMember(@Valid MemberDto.Post requestBody) {
        Member member = mapper.memberPostToMember(requestBody);
        memberService.createMember(member);

        System.out.println("Member Registration Successfully");
        return "login";  // 회원가입 성공했을 때 login.html로 보내는 거야
    }

    @GetMapping("/my-page")
    public String myPage() {
        return "my-page";
    }
}
