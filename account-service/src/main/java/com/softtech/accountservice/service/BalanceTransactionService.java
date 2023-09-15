package com.softtech.accountservice.service;

import com.softtech.accountservice.dto.BalanceTransactionDetailDto;
import com.softtech.accountservice.dto.BalanceTransactionDto;
import com.softtech.accountservice.dto.BalanceUpdateDto;
import jakarta.servlet.http.HttpServletRequest;

public interface BalanceTransactionService {
    double getCurrentBalance(Long id);

    void createFirstRow(Long memberId);

    BalanceTransactionDetailDto balanceUpdate(HttpServletRequest request, BalanceUpdateDto dto);

    Boolean balanceTransaction(HttpServletRequest request, BalanceTransactionDto dto);
}
