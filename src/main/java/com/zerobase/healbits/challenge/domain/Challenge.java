package com.zerobase.healbits.challenge.domain;

import com.zerobase.healbits.domain.BaseEntity;
import com.zerobase.healbits.member.domain.Member;
import com.zerobase.healbits.type.ChallengeCategory;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Challenge extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
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

}
