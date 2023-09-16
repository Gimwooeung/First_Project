package com.sparta.first_project.entity;

import com.sparta.first_project.dto.ProfileRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String intro;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    public void updateprofile(ProfileRequestDto profileRequestDto) {
        this.password = profileRequestDto.getPassword();
        this.email = profileRequestDto.getEmail();
        this.nickname = profileRequestDto.getNickname();
        this.intro = profileRequestDto.getIntro();
    }

}