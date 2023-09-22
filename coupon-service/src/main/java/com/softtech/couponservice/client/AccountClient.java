package com.softtech.couponservice.client;

import com.softtech.couponservice.dto.BalanceTransactionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "account-service", url = "${application.config.balance-controller-url}")
public interface AccountClient {

    @PostMapping("/transactions")
    Boolean balanceTransaction(@RequestBody BalanceTransactionDto dto);
}
