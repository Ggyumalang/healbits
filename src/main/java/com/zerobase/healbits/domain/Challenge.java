package com.zerobase.healbits.domain;

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
    private Member member;

    @Enumerated(EnumType.STRING)
    private ChallengeCategory challengeCategory;

    private String summary;

    @Lob
    private String contents;

    private long participantsNum;

    private LocalDate startDate;

    private LocalDate endDate;

}
