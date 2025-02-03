package com.springboot.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

//RestController : controller + ResponseBoy(응답데이터를 JSON 으로 직렬화
    // resource 에서 찾아요 -> templates - Login.html로 이동
@Controller
@RequestMapping("/auths")
public class AuthController {
    @GetMapping("/login-form")
    public String loginForm() {
        return "login";
    }


    @GetMapping("/access-denied")
    public String accessDinied() {
        //문자열 기반의 HTml 파일을 찾아서 반환
        return "access-denied";
    }


    @PostMapping("/login")
    public String login() {
        // TODO 아이디, 패스워드 받아서 인증 처리를 해야 한다.
        System.out.println("Login successfully!");
        return "home";
    }
}
