package com.pet.petmily.user.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data   //getter,setter,toString,equals,hashCode,RequiredArgsConstructor
public class MemberUpdateDTO {

    private String email;
    private String nickname;
    LocalDateTime lastModifiedDate;
}
