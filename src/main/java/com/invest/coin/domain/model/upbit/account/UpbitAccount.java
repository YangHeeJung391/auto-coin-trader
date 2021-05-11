package com.invest.coin.domain.model.upbit.account;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UpbitAccount {
	
	private String currency;
	private BigDecimal balance;
	private BigDecimal locked;
	@JsonProperty("avg_buy_price")
	private BigDecimal avgBuyPrice;
	private Boolean modified;

}
