package com.zerobase.healbits.member.domain;

import com.zerobase.healbits.common.domain.BaseEntity;
import com.zerobase.healbits.common.exception.HealBitsException;
import lombok.*;

import javax.persistence.*;

import static com.zerobase.healbits.common.type.ErrorCode.AMOUNT_EXCEED_BALANCE;
import static com.zerobase.healbits.common.type.ErrorCode.INVALID_AMOUNT;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String email;

    private String password;

    private String name;

    private String phone;

    private long balance;

    public void chargeBalance(long amount) {
        if (amount <= 0) {
            throw new HealBitsException(INVALID_AMOUNT);
        }
        this.balance += amount;
    }

    public void useBalance(long amount) {
        if (amount <= 0) {
            throw new HealBitsException(INVALID_AMOUNT);
        }

        if (amount > this.balance) {
            throw new HealBitsException(AMOUNT_EXCEED_BALANCE);
        }
        this.balance -= amount;
    }

}
