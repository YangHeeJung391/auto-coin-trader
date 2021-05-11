package com.invest.coin.domain.model.upbit.market;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpbitOrderbookUnit {
	@JsonProperty("ask_price")
	private BigDecimal askPrice;
	@JsonProperty("bid_price")
	private BigDecimal bidPrice;
	@JsonProperty("ask_size")
	private BigDecimal askSize;
	@JsonProperty("bid_size")
	private BigDecimal bidSize;

}
