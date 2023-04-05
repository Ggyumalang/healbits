package com.zerobase.healbits.transaction.dto;

import com.zerobase.healbits.common.type.TransactionResultType;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class UseBalance {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @NotNull
        @Min(10)
        @Max(1000_000_000)
        private long participationFee;
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private String email;
        private TransactionResultType transactionResultType;
        private String transactionId;
        private long participationFee;
        private long balanceSnapshot;

        public static Response from(TransactionDto transactionDto) {
            return Response.builder()
                    .email(transactionDto.getEmail())
                    .transactionResultType(transactionDto.getTransactionResultType())
                    .transactionId(transactionDto.getTransactionId())
                    .participationFee(transactionDto.getAmount())
                    .balanceSnapshot(transactionDto.getBalanceSnapshot())
                    .build();
        }
    }
}
