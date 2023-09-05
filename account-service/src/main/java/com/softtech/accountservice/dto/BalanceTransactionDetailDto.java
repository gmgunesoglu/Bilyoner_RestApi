package com.softtech.accountservice.dto;

import com.softtech.accountservice.entity.TransactionType;
import lombok.Data;

import java.util.Date;

@Data
public class BalanceTransactionDetailDto {

    private Date transactionDate;
    private TransactionType transactionType;
    private double oldAmount;
    private double transactionAmount;
    private double newAmount;
}
