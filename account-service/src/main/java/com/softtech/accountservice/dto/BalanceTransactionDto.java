package com.softtech.accountservice.dto;

import lombok.Data;

@Data
public class BalanceTransactionDto {

    private TransactionCouponType transactionType;
    private double transactionAmount;
}
