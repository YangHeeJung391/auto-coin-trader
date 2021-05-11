package com.invest.coin.domain.model.upbit.trade;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpbitOrderChanceMarket {
	@JsonProperty("id")
	private String id;
	@JsonProperty("bid")
	private UpbitOrderChanceMarketSide bid;
	@JsonProperty("ask")
	private UpbitOrderChanceMarketSide ask;

}
