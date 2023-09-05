package com.softtech.accountservice.dto;

import com.softtech.accountservice.entity.TransactionType;
import lombok.Data;

@Data
public class BalanceTransactionDto {

    private TransactionType transactionType;
    private double transactionAmount;
}
