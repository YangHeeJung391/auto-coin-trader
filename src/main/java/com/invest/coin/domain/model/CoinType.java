package com.invest.coin.domain.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CoinType {
	BTC("KRW-BTC", new BigDecimal(1000), "��Ʈ����"),
	ETH("KRW-ETH", new BigDecimal(500), "�̴�����"),
	BCH("KRW-BCH", new BigDecimal(50), "��Ʈ����ĳ��"),
	EOS("KRW-EOS", new BigDecimal(5), "�̿���"),
	DOT("KRW-DOT", new BigDecimal(10), "��ī��");
	
	private String upbitTicker;
	private BigDecimal priceUnit;
	private String name;
	

}
