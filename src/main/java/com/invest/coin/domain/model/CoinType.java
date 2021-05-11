package com.invest.coin.domain.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CoinType {
	BTC("KRW-BTC", new BigDecimal(1000), "비트코인"),
	ETH("KRW-ETH", new BigDecimal(500), "이더리움"),
	BCH("KRW-BCH", new BigDecimal(50), "비트코인캐시"),
	EOS("KRW-EOS", new BigDecimal(5), "이오스"),
	DOT("KRW-DOT", new BigDecimal(10), "폴카닷");
	
	private String upbitTicker;
	private BigDecimal priceUnit;
	private String name;
	

}
