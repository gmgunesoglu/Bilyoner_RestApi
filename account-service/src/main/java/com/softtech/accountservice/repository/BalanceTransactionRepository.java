package com.softtech.accountservice.repository;

import com.softtech.accountservice.entity.BalanceTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceTransactionRepository extends JpaRepository<BalanceTransaction,Long> {
}
