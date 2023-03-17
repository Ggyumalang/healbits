package com.zerobase.healbits.verification.domain;

import com.zerobase.healbits.domain.BaseEntity;
import com.zerobase.healbits.takechallenge.domain.TakeChallenge;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
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
