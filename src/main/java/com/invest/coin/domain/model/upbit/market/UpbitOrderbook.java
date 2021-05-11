package com.invest.coin.domain.model.upbit.market;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpbitOrderbook {
	private String market;
	private Long timestamp;
	@JsonProperty("total_ask_size")
	private BigDecimal totalAskSize;
	@JsonProperty("total_bid_size")
	private BigDecimal totalBidSize;
	@JsonProperty("orderbook_units")
	private List<UpbitOrderbookUnit> orderbookUnits;

}
