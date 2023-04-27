package com.pet.petmily.user.controller;

import com.pet.petmily.user.dto.MemberLoginDTO;
import com.pet.petmily.user.dto.MemberSignUpDto;
import com.pet.petmily.user.dto.MemberUpdateDTO;
import com.pet.petmily.user.entity.BaseTimeEntity;
import com.pet.petmily.user.entity.Member;
import com.pet.petmily.user.handler.LoginSuccessHandler;
import com.pet.petmily.user.service.LoginService;
import com.pet.petmily.user.service.MemberService;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MemberController extends BaseTimeEntity {


    private final MemberService memberService;




    @PostMapping("/sign-up")
    public String signUp(@RequestBody MemberSignUpDto memberSignUpDto) throws Exception {
        memberService.signUp(memberSignUpDto);
        return "회원가입 성공";
    }
    @GetMapping("/user/info")
    @ResponseBody
    public Object userInfo(Principal principal,MemberLoginDTO memberLoginDTO){
        String email=principal.getName();
        String nickName=memberService.getNickName(email);
        LocalDateTime createDate=memberService.getCreateDate(email);
        memberLoginDTO.setEmail(email);
        memberLoginDTO.setNickname(nickName);
        memberLoginDTO.setCreateDate(createDate);

        return memberLoginDTO;

    }
    @PatchMapping("/user/update")
    //유저 정보 업데이트(수정)
    public String update(Principal principal,@RequestBody MemberUpdateDTO memberUpdateDTO) throws Exception {
        log.info("업데이트 요청");
        String email=principal.getName();
        memberService.updateMember(email,memberUpdateDTO);

        return "업데이트 성공";
    }




    











    @GetMapping("/jwt-test")
    public String jwtTest() {

        return "jwtTest 요청 성공";
    }
}
