package com.invest.coin.domain.model.quant.momentum.volatility_range_breakout;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VloatilityRangeBreakoutStatus {
	BREAKOUT_REQUEST("BB"),
	NOT_BREAKOUT("NB"),
	SELL_REQUEST("SR"),
	SELL_DONE("SD"),
	BUY_REQURST("BR"),
	BUY_DONE("BD");
	
	private String code;

}
