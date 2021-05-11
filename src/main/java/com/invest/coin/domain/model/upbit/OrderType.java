package com.invest.coin.domain.model.upbit;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderType {
	BID("bid", "매수"),
	ASK("ask", "매도");
	
	private String code;
	private String description;

}
