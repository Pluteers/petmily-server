package com.pet.petmily.user.entity;

import lombok.*;

import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;




@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
@AllArgsConstructor
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "email")
    private String email;
    private String password;
    private String nickname;//닉네임

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;//소셜 로그인 타입,자체 로그인 시는 null(즉 nullable해야함)
    private String socialId;//소셜 로그인 아이디,자체 로그인 시는 null(즉 nullable해야함)

    private String refreshToken;
    private boolean status;


    // 유저 권한 설정 메소드
    public void authorizeUser() {

        this.role = Role.USER;
    }
    // 비밀번호 암호화 메소드
    public void passwordEncode(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    //리프레시 토큰 메소드
    public void updateRefreshToken(String updateRefreshToken) {

        this.refreshToken = updateRefreshToken;
    }

}
