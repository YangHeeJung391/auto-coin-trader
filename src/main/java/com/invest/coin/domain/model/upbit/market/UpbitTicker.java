package com.invest.coin.domain.model.upbit.market;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UpbitTicker {
	private String market;
	@JsonProperty("trade_date_kst")
	private String tradeDateKst;
	@JsonProperty("trade_time_kst")
	private String tradeTimeKst;
	@JsonProperty("trade_timestamp")
	private Long tradeTimestamp;
	@JsonProperty("opening_price")
	private BigDecimal openingPrice;
	@JsonProperty("high_price")
	private BigDecimal highPrice;
	@JsonProperty("low_price")
	private BigDecimal lowPrice;
	@JsonProperty("trade_price")
	private BigDecimal tradePrice;
	@JsonProperty("prev_closing_price")
	private BigDecimal prevClosingPrice;
	private Long timestamp;

}
