package com.pet.petmily.user.controller;

import com.pet.petmily.user.dto.MemberSignUpDto;
import com.pet.petmily.user.service.MemberService;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {


    private final MemberService memberService;



    @PostMapping("/sign-up")
    public String signUp(@RequestBody MemberSignUpDto memberSignUpDto) throws Exception {
        memberService.signUp(memberSignUpDto);
        return "redirect:signUpSuccess";
    }

    @GetMapping("/jwt-test")
    public String jwtTest() {

        return "jwtTest 요청 성공";
    }
}
