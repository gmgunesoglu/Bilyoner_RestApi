package com.softtech.accountservice.repository;

import com.softtech.accountservice.entity.BalanceTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BalanceTransactionRepository extends JpaRepository<BalanceTransaction,Long> {

    @Query("SELECT bt.newBalance FROM BalanceTransaction bt WHERE bt.memberId = :memberId " +
            "ORDER BY bt.transactionDate DESC LIMIT 1")
    double getCurrentBalance(Long memberId);
}
