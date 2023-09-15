package com.softtech.accountservice.service;

import com.softtech.accountservice.dto.*;
import com.softtech.accountservice.entity.BalanceTransaction;
import com.softtech.accountservice.entity.TransactionType;
import com.softtech.accountservice.exceptionhandling.GlobalRuntimeException;
import com.softtech.accountservice.repository.BalanceTransactionRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class BalanceTransactionServiceImpl implements BalanceTransactionService{

    private final BalanceTransactionRepository repository;

    private final JwtUtil jwtService;

    @Override
    public double getCurrentBalance(Long memberId) {
        return repository.getCurrentBalance(memberId);
    }

    @Override
    public void createFirstRow(Long memberId) {
        BalanceTransaction balanceTransaction = new BalanceTransaction();
        balanceTransaction.setOldBalance(0);
        balanceTransaction.setNewBalance(0);
        balanceTransaction.setTransactionAmount(0);
        balanceTransaction.setTransactionDate(new Date(System.currentTimeMillis()));
        balanceTransaction.setTransactionType(TransactionType.OPEN_ACCOUNT);
        balanceTransaction.setMemberId(memberId);
        repository.save(balanceTransaction);
    }

    @Override
    public BalanceTransactionDetailDto balanceUpdate(HttpServletRequest request, BalanceUpdateDto dto) {

        // get member id from request
        String jwt = request.getHeader("Authorization").substring(7);
        Long memberId = jwtService.getId(jwt);

        // get current balance
        double oldBalance = getCurrentBalance(memberId);

        // check new balance
        double newBalance;
        if(dto.getTransactionType() == TransactionMoveType.WITHDRAW_AMOUNT) {
            newBalance = oldBalance-dto.getTransactionAmount();
            if(newBalance<0){
                throw new GlobalRuntimeException("Can't withdraw amount higher than current amount." +
                        "\nCurren amount: "+oldBalance, HttpStatus.BAD_REQUEST);
            }
        }else{
            newBalance = oldBalance+dto.getTransactionAmount();
        }

        // create and save BalanceTransaction
        BalanceTransaction balanceTransaction = new BalanceTransaction();
        balanceTransaction.setOldBalance(oldBalance);
        balanceTransaction.setNewBalance(newBalance);
        balanceTransaction.setTransactionAmount(dto.getTransactionAmount());
        balanceTransaction.setTransactionDate(new Date(System.currentTimeMillis()));
        balanceTransaction.setTransactionType(
                TransactionType.fromTransactionMoveType(dto.getTransactionType())
        );
        balanceTransaction.setMemberId(memberId);
        repository.save(balanceTransaction);

        // create dto and return
        BalanceTransactionDetailDto rDto = new BalanceTransactionDetailDto();
        rDto.setTransactionDate(balanceTransaction.getTransactionDate());
        rDto.setTransactionType(balanceTransaction.getTransactionType());
        rDto.setOldAmount(balanceTransaction.getOldBalance());
        rDto.setTransactionAmount(balanceTransaction.getTransactionAmount());
        rDto.setNewAmount(balanceTransaction.getNewBalance());
        return rDto;
    }


    @Override
    public Boolean balanceTransaction(HttpServletRequest request, BalanceTransactionDto dto) {

        // get member id from request
        String jwt = request.getHeader("Authorization").substring(7);
        Long memberId = jwtService.getId(jwt);

        // get current balance
        double oldBalance = getCurrentBalance(memberId);

        // check new balance
        double newBalance;
        if(dto.getTransactionType().equals("COUPON_PURCHASE")) {
            newBalance = oldBalance-dto.getTransactionAmount();
            if(newBalance<0){
                return false;
            }
        }else{
            newBalance = oldBalance+dto.getTransactionAmount();
        }

        // create and save BalanceTransaction
        BalanceTransaction balanceTransaction = new BalanceTransaction();
        balanceTransaction.setOldBalance(oldBalance);
        balanceTransaction.setNewBalance(newBalance);
        balanceTransaction.setTransactionAmount(dto.getTransactionAmount());
        balanceTransaction.setTransactionDate(new Date(System.currentTimeMillis()));
        balanceTransaction.setTransactionType(
                TransactionType.fromTransactionCouponType(dto.getTransactionType())
        );
        balanceTransaction.setMemberId(memberId);
        repository.save(balanceTransaction);

        return true;
    }
}
