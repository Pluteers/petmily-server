package com.pet.petmily.user.dto;

import com.pet.petmily.user.entity.BaseTimeEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class MemberSignUpDto extends BaseTimeEntity{

    private String email;
    private String password;
    private String nickname;
    private boolean status;



}
