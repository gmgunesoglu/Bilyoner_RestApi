package com.softtech.accountservice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "balance_transaction")
@Data
public class BalanceTransaction {

    @Id
    @SequenceGenerator(
            name = "balance_transaction_seq",
            sequenceName = "balance_transaction_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "balance_transaction_seq"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    //one to many
    @Column(
            name = "member_id",
            nullable = false
    )
    private Long memberId;

    @Column(
            name = "transaction_date",
            nullable = false
    )
    private Date transactionDate;

    @Column(
            name = "transaction_type",
            length = 15,
            nullable = false
    )
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(
            name = "old_balance",
            nullable = false
    )
    private double oldBalance;

    @Column(
            name = "transaction_amount",
            nullable = false
    )
    private double transactionAmount;

    @Column(
            name = "new_balance",
            nullable = false
    )
    private double newBalance;
}
