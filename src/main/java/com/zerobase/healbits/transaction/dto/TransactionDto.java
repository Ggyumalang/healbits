package com.zerobase.healbits.transaction.dto;

import com.zerobase.healbits.transaction.domain.Transaction;
import com.zerobase.healbits.transaction.type.TransactionResultType;
import com.zerobase.healbits.transaction.type.TransactionType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDto {

    private String email;

    private TransactionType transactionType;

    private TransactionResultType transactionResultType;

    private String transactionId;

    private long amount;

    private long balanceSnapshot;

    private LocalDateTime transactedDateTime;

    public static TransactionDto fromEntity(Transaction transaction) {
        return TransactionDto.builder()
                .email(transaction.getMember().getEmail())
                .transactionType(transaction.getTransactionType())
                .transactionResultType(transaction.getTransactionResultType())
                .transactionId(transaction.getTransactionId())
                .amount(transaction.getAmount())
                .balanceSnapshot(transaction.getBalanceSnapshot())
                .transactedDateTime(transaction.getTransactedDateTime())
                .build();
    }
}
