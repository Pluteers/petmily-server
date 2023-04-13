package com.pet.petmily.user.dto;

import com.pet.petmily.user.entity.BaseTimeEntity;
import lombok.Data;

import java.sql.Timestamp;


@Data
public class MemberSignUpDto {

    private String email;
    private String password;
    private String name;
    private String phone;
    private String nickname;
    private Timestamp createdDate;



}
