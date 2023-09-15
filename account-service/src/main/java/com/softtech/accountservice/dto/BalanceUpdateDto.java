package com.softtech.accountservice.dto;

import lombok.Data;

@Data
public class BalanceUpdateDto {
    private TransactionMoveType transactionType;
    private double transactionAmount;
}
