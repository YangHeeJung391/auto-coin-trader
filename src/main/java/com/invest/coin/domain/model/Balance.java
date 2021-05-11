package com.invest.coin.domain.model;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class Balance {
	private BigDecimal totalPurchaseAmount = BigDecimal.ZERO;
	private BigDecimal balance = BigDecimal.ZERO;
	
	public BigDecimal getTotalAmount() {
		return totalPurchaseAmount.add(balance);
	}

}
