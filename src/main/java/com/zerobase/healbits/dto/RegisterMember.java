package com.zerobase.healbits.dto;

import com.zerobase.healbits.validator.PhoneNumCheck;
import lombok.*;

import javax.validation.constraints.NotBlank;

public class RegisterMember {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Request {
        @NotBlank
        private String email;

        @NotBlank
        private String password;

        @NotBlank
        private String name;

        @NotBlank
        @PhoneNumCheck
        private String phone;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private String email;
        private String name;

        public static Response from(MemberDto memberDto) {
            return Response.builder()
                    .email(memberDto.getEmail())
                    .name(memberDto.getName())
                    .build();
        }

    }


}
