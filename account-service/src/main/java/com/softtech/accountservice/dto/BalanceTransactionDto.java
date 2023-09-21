package com.softtech.accountservice.dto;

import lombok.Data;

@Data
public class BalanceTransactionDto {

    private Long memberId;
    private TransactionCouponType transactionType;
    private double transactionAmount;
}
