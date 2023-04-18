package com.pet.petmily.user.controller;

import com.pet.petmily.user.dto.MemberLoginDTO;
import com.pet.petmily.user.dto.MemberSignUpDto;
import com.pet.petmily.user.handler.LoginSuccessHandler;
import com.pet.petmily.user.service.LoginService;
import com.pet.petmily.user.service.MemberService;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MemberController {


    private final MemberService memberService;



    @PostMapping("/sign-up")
    public String signUp(@RequestBody MemberSignUpDto memberSignUpDto) throws Exception {
        memberService.signUp(memberSignUpDto);
        return "redirect:signUpSuccess";
    }
    @GetMapping("/user")
    public ResponseEntity<String> user() {
        return ResponseEntity.ok("user");

    }







    @GetMapping("/jwt-test")
    public String jwtTest() {

        return "jwtTest 요청 성공";
    }
}
