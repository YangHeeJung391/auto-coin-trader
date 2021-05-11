package com.invest.coin.domain.service.upbit.account;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.invest.coin.config.FeignRetryConfig;
import com.invest.coin.domain.model.upbit.account.UpbitAccount;

@FeignClient(name = "upbit-api", contextId="upbitAccountClient", url = "${upbit.host}")
public interface UpbitAccountClient {
	
	@GetMapping("/v1/accounts")
	List<UpbitAccount> getAccounts(@RequestHeader("Authorization") String authorizationToken);

}
