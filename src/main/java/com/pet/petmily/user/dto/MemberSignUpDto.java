package com.pet.petmily.user.dto;

import com.pet.petmily.user.entity.BaseTimeEntity;

import com.pet.petmily.user.entity.SocialType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class MemberSignUpDto extends BaseTimeEntity{

    private String email;
    private String password;
    private String nickname;
    private boolean status;
    private SocialType socialType;
    private String socialId;



}
