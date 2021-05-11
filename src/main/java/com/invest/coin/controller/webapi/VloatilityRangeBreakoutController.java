package com.invest.coin.controller.webapi;

import java.math.BigDecimal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.invest.coin.domain.model.CoinType;
import com.invest.coin.domain.service.quant.momentum.volatility_range_breakout.VloatilityRangeBreakoutBuyService;
import com.invest.coin.domain.service.quant.momentum.volatility_range_breakout.VloatilityRangeBreakoutSellService;
import com.invest.coin.domain.service.quant.momentum.volatility_range_breakout.VloatilityRangeBreakoutService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class VloatilityRangeBreakoutController {
	
	private final VloatilityRangeBreakoutService vloatilityRangeBreakoutService;
	private final VloatilityRangeBreakoutBuyService vloatilityRangeBreakoutBuyService;
	private final VloatilityRangeBreakoutSellService vloatilityRangeBreakoutSellService;
	
	@GetMapping("/coin/calculate")
	public String order(BigDecimal targetVloatilityRate) {
		for(CoinType coinType : CoinType.values()) {
			vloatilityRangeBreakoutService.calculate(coinType, targetVloatilityRate);
		}
		return "calculate";
	}
	
	@GetMapping("/coin/check_and_buy")
	public String checkAndBuy() {
		for(CoinType coinType : CoinType.values()) {
			vloatilityRangeBreakoutBuyService.checkAndBuy(coinType);
		}
		return "calculate";
	}
	
	@GetMapping("/coin/sell")
	public String sell() {
		for(CoinType coinType : CoinType.values()) {
			vloatilityRangeBreakoutSellService.sell(coinType);
		}
		return "sell";
	}

}
