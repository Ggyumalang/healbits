package com.zerobase.healbits.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginMember {
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
