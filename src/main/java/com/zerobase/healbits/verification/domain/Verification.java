package com.zerobase.healbits.verification.domain;

import com.zerobase.healbits.domain.BaseEntity;
import com.zerobase.healbits.takechallenge.domain.TakeChallenge;
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
public class Verification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "verified_take_challenge_id")
    private TakeChallenge verifiedTakeChallenge;

    private LocalDate verifiedDate;

    private String imagePath;

}
