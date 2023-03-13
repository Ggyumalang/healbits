package com.zerobase.healbits.member.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginMember {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
}
