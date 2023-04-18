package com.pet.petmily.user.service;

import com.pet.petmily.user.dto.MemberSignUpDto;
import com.pet.petmily.user.entity.Member;
import com.pet.petmily.user.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.pet.petmily.user.repository.MemberRepository;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor

public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(MemberSignUpDto memberSignUpDto) throws Exception{

        if(memberRepository.findByEmail(memberSignUpDto.getEmail()).isPresent()){
            throw new Exception("이미 존재하는 이메일입니다.");
        }
        if(memberRepository.findByNickname(memberSignUpDto.getNickname()).isPresent()){
            throw new Exception("이미 존재하는 닉네임입니다.");
        }
        Member member= Member.builder()
                .email(memberSignUpDto.getEmail())
                .password(passwordEncoder.encode(memberSignUpDto.getPassword()))
                .nickname(memberSignUpDto.getNickname())
                .role(Role.USER)
                .status(true)

                .build();
        //member.passwordEncode(passwordEncoder);
        memberRepository.save(member);
    }


}
