package com.zerobase.healbits.transaction.domain;

import com.zerobase.healbits.common.domain.BaseEntity;
import com.zerobase.healbits.member.domain.Member;
import com.zerobase.healbits.common.type.TransactionResultType;
import com.zerobase.healbits.common.type.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Transaction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    private TransactionResultType transactionResultType;

    @Column(unique = true)
    private String transactionId;

    private long amount;

    private long balanceSnapshot;

    private LocalDateTime transactedDateTime;


}
