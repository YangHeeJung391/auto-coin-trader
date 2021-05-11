package com.invest.coin.domain.model.upbit.trade;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.invest.coin.domain.model.upbit.UpbitError;

import lombok.Data;

@Data
public class UpbitOrderChance {
	@JsonProperty("error")
	private UpbitError error;
	@JsonProperty("bid_fee")
	private BigDecimal bidFee;
	@JsonProperty("ask_fee")
	private BigDecimal askFee;
	@JsonProperty("market")
	private UpbitOrderChanceMarket market;

}