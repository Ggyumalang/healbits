package com.zerobase.healbits.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class RegisterMember {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Request{
        @NotBlank
        private String email;

        @NotBlank
        private String password;

        @NotBlank
        private String name;

        @NotBlank
        private String phone;

        @NotNull
        private long balance;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response{
        private String email;
        private String name;

        public static Response from(MemberDto memberDto){
            return Response.builder()
                    .email(memberDto.getEmail())
                    .name(memberDto.getName())
                    .build();
        }

    }




}
