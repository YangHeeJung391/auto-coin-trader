package com.invest.coin.domain.model.upbit.trade;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpbitOrderChanceMarketSide {
	@JsonProperty("currency")
	private String id;
	@JsonProperty("price_unit")
	private BigDecimal priceUnit;
	@JsonProperty("min_total")
    private BigDecimal minTotal;

}
