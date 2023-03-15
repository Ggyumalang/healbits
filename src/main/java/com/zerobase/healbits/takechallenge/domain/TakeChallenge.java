package com.zerobase.healbits.takechallenge.domain;

import com.zerobase.healbits.challenge.domain.Challenge;
import com.zerobase.healbits.domain.BaseEntity;
import com.zerobase.healbits.member.domain.Member;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table (
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"participated_member_Id", "participated_challenge_id"}
                )
        }
)
@Entity
public class TakeChallenge extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Member participatedMember;

    @ManyToOne
    private Challenge participatedChallenge;

    private long participationFee;

}
