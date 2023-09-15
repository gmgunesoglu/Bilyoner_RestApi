package com.softtech.accountservice.controller;

import com.softtech.accountservice.dto.BalanceTransactionDetailDto;
import com.softtech.accountservice.dto.BalanceTransactionDto;
import com.softtech.accountservice.dto.BalanceUpdateDto;
import com.softtech.accountservice.service.BalanceTransactionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/balance")
@RequiredArgsConstructor
public class BalanceController {

    private final BalanceTransactionService balanceTransactionService;

    @PostMapping("/update")
    BalanceTransactionDetailDto balanceUpdate(HttpServletRequest request, @RequestBody BalanceUpdateDto dto){
        return balanceTransactionService.balanceUpdate(request,dto);
    }

    @PostMapping("/transaction")
    Boolean balanceTransaction(HttpServletRequest request, @RequestBody BalanceTransactionDto dto){
        return balanceTransactionService.balanceTransaction(request,dto);
    }
}
