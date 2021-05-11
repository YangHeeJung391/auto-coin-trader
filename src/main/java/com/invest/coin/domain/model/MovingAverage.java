package com.invest.coin.domain.model;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class MovingAverage {
	private BigDecimal openingPrice;
	private BigDecimal closePrice;
    private BigDecimal highPrice;
    private BigDecimal lowPrice;
    private BigDecimal tradePrice;

}
