package com.invest.coin.domain.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CoinType {
	BTC("KRW-BTC", new BigDecimal(1000), "��Ʈ����"),
	ETH("KRW-ETH", new BigDecimal(1000), "�̴�����");
	
	private String upbitTicker;
	private BigDecimal priceUnit;
	private String name;
	

}
