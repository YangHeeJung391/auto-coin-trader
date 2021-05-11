package com.invest.coin.domain.service.upbit.account;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.stereotype.Service;

import com.invest.coin.domain.model.Balance;
import com.invest.coin.domain.model.upbit.account.UpbitAccount;
import com.invest.coin.domain.service.upbit.UpbitTokenService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpbitAccountService {
	
	private final UpbitTokenService upbitTokenService;
	private final UpbitAccountClient upbitAccountClient;
	
	public Balance getUpbitBalance() {
		Balance balance = new Balance();
		List<UpbitAccount> upbitAccounts = upbitAccountClient.getAccounts(upbitTokenService.createAuthToken());
		for (UpbitAccount upbitAccount : upbitAccounts) {
			if ("KRW".equals(upbitAccount.getCurrency())) {
				balance.setBalance(upbitAccount.getBalance().setScale(0, RoundingMode.HALF_UP));
			} else {
				BigDecimal purchaseAmount = upbitAccount.getAvgBuyPrice().multiply(upbitAccount.getBalance());
				balance.setTotalPurchaseAmount(balance.getTotalPurchaseAmount()
						.add(purchaseAmount));
			}
		}
		balance.setTotalPurchaseAmount(balance.getTotalPurchaseAmount().setScale(0, RoundingMode.HALF_UP));
		return balance;
	}

}
