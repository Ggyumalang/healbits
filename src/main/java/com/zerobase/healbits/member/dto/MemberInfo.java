package com.zerobase.healbits.member.dto;

import com.zerobase.healbits.member.domain.Member;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberInfo {

    private String email;
    private String name;
    private String phone;
    private long balance;

    public static MemberInfo fromEntity(Member member) {
        return MemberInfo.builder()
                .email(member.getEmail())
                .name(member.getName())
                .phone(member.getPhone())
                .balance(member.getBalance())
                .build();
    }
}
