package com.invest.coin.domain.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CoinType {
	BTC("KRW-BTC", new BigDecimal(1000), "비트코인"),
	ETH("KRW-ETH", new BigDecimal(1000), "이더리움");
	
	private String upbitTicker;
	private BigDecimal priceUnit;
	private String name;
	

}
