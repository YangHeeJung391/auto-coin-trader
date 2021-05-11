package com.invest.coin.domain.model.upbit.trade;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.invest.coin.domain.model.upbit.UpbitError;

import lombok.Data;

@Data
public class UpbitOrderTrade {
	private String market;
	private BigDecimal price;
	private BigDecimal volume;
	private String side;

}
