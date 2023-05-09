package com.pet.petmily.user.service;

import com.pet.petmily.user.dto.MemberSignUpDto;
import com.pet.petmily.user.dto.MemberUpdateDTO;
import com.pet.petmily.user.entity.Member;
import com.pet.petmily.user.entity.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.pet.petmily.user.repository.MemberRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j

public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;




    public String signUp(MemberSignUpDto memberSignUpDto) throws Exception{

        if(memberRepository.findByEmail(memberSignUpDto.getEmail()).isPresent()){
            if(memberRepository.findByEmail(memberSignUpDto.getEmail()).get().getSocialType()!=null){
                return "redirect:/login";

            }
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
                .socialType(memberSignUpDto.getSocialType())
                .socialId(memberSignUpDto.getSocialId())




                .build();
        //member.passwordEncode(passwordEncoder);
        memberRepository.save(member);
        return null;
    }


    public String getNickName(String email) {
        return memberRepository.findByEmail(email).get().getNickname();
    }

    public LocalDateTime getCreateDate(String email) {
        return memberRepository.findByEmail(email).get().getCreateDate();
    }
    public Long getId(String email) {
        return memberRepository.findByEmail(email).get().getId();
    }
    @Transactional
    public void updateMember(String email,MemberUpdateDTO memberUpdateDTO) throws Exception{
        Member member=memberRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("해당 이메일이 존재하지 않습니다."));
        member.updateMember(memberUpdateDTO);


    }


}
