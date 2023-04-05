package com.zerobase.healbits.challenge.domain;

import com.zerobase.healbits.common.domain.BaseEntity;
import com.zerobase.healbits.member.domain.Member;
import com.zerobase.healbits.common.type.ChallengeCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Challenge extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "registered_member_id")
    private Member registeredMember;

    private String challengeName;

    @Enumerated(EnumType.STRING)
    private ChallengeCategory challengeCategory;

    private String summary;

    @Lob
    private String contents;

    private long participantsNum;

    private LocalDate startDate;

    private LocalDate endDate;

    public void increaseParticipantsNum() {
        participantsNum += 1;
    }

}
