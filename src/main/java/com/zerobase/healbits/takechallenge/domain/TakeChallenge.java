package com.zerobase.healbits.takechallenge.domain;

import com.zerobase.healbits.challenge.domain.Challenge;
import com.zerobase.healbits.domain.BaseEntity;
import com.zerobase.healbits.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
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
    @JoinColumn(name = "participated_member_id")
    private Member participatedMember;

    @ManyToOne
    @JoinColumn(name = "participated_challenge_id")
    private Challenge participatedChallenge;

    private long participationFee;

    public void completeTookChallenge(){
        this.participationFee = -1;
    }

}
