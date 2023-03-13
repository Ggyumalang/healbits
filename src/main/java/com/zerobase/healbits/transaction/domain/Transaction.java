package com.zerobase.healbits.transaction.domain;

import com.zerobase.healbits.domain.BaseEntity;
import com.zerobase.healbits.member.domain.Member;
import com.zerobase.healbits.type.TransactionResultType;
import com.zerobase.healbits.type.TransactionType;
import lombok.*;

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
    private Member member;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    private TransactionResultType transactionResultType;

    private String transactionId;

    private long amount;

    private long balanceSnapshot;

    private LocalDateTime transactedDateTime;


}
