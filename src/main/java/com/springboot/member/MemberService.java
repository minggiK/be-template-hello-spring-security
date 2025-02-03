package com.springboot.member;

//인메모리 저장 -> DB에 저장할 수 있으니까 인터페이스로 구현
public interface MemberService {
   //passwordEncoder를 통해서 암호화 해야한다.
    //암호화하는 로직은 인메모리, 디비에 저장할지 나누어져 있는것 (구현체로 각각 구현되어있음)
    Member createMember(Member member);
}
