package com.zerobase.healbits.dto;

import com.zerobase.healbits.domain.Member;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDto {

    private String email;
    private String name;
    private String phone;
    private long balance;

    public static MemberDto fromEntity(Member member){
        return MemberDto.builder()
                .email(member.getEmail())
                .name(member.getName())
                .phone(member.getPhone())
                .balance(member.getBalance())
                .build();
    }

}
