package com.invest.coin.domain.scheduler;

import java.math.BigDecimal;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.invest.coin.domain.model.CoinType;
import com.invest.coin.domain.service.quant.momentum.volatility_range_breakout.VloatilityRangeBreakoutBuyService;
import com.invest.coin.domain.service.quant.momentum.volatility_range_breakout.VloatilityRangeBreakoutSellService;
import com.invest.coin.domain.service.quant.momentum.volatility_range_breakout.VloatilityRangeBreakoutService;
import com.invest.coin.domain.service.quant.momentum.volatility_range_breakout.VloatilityRangeBreakoutStoplossService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CheckScheduler {
	
	private final VloatilityRangeBreakoutService vloatilityRangeBreakoutService;
	private final VloatilityRangeBreakoutBuyService vloatilityRangeBreakoutBuyService;
	private final VloatilityRangeBreakoutSellService vloatilityRangeBreakoutSellService;
	private final VloatilityRangeBreakoutStoplossService vloatilityRangeBreakoutStoplossService;
	
	// 매수 할지 체크 ( 1초에 한번 실행 )
	@Scheduled(fixedDelay = 1000) 
	public void checkAndBuy() {
		try {
			for(CoinType coinType : CoinType.values()) {
				vloatilityRangeBreakoutBuyService.checkAndBuy(coinType);
			}
		} catch (Exception e) {
			e.printStackTrace();
			vloatilityRangeBreakoutBuyService.setChecking(false);
		}
		
	}
	
	// 매수 체결 확인 ( 1분에 한번 실행 )
	@Scheduled(fixedDelay = 1000 * 60) 
	public void confirmBuyOrder() {
		for(CoinType coinType : CoinType.values()) {
			vloatilityRangeBreakoutBuyService.confirmBuyOrder(coinType);
		}
	}
	
	// 매도 체결 확인 ( 1분에 한번 실행 )
	@Scheduled(fixedDelay = 1000 * 60) 
	public void confirmSellOrder() {
		for(CoinType coinType : CoinType.values()) {
			vloatilityRangeBreakoutSellService.confirmSellOrder(coinType);
		}
	}
	
	// 손절 여부 확인 ( 5초에 한번 실행 , 구매가격 기준 5% 이상 손실시 손절 )
	@Scheduled(fixedDelay = 1000 * 5) 
	public void checkStopLossAndSellOrder() {
		try {
			for(CoinType coinType : CoinType.values()) {
				vloatilityRangeBreakoutStoplossService.checkStopLossAndSellOrder(coinType);
			}
		} catch (Exception e) {
			e.printStackTrace();
			vloatilityRangeBreakoutStoplossService.setChecking(false);
		}
	}
	
	// 매수할 가격 계산 (매 정시 1시간마다)
	@Scheduled(cron = "0 1 * * * *") 
	public void calculate() {
		BigDecimal targetVloatilityRate = BigDecimal.valueOf(0.04);
		for(CoinType coinType : CoinType.values()) {
			vloatilityRangeBreakoutService.calculate(coinType, targetVloatilityRate);
		}
	}
	
	// 매도 (매 정시 1시간마다)
	@Scheduled(cron = "0 0 * * * *") 
	public void sell() {
		for(CoinType coinType : CoinType.values()) {
			vloatilityRangeBreakoutSellService.sell(coinType);
		}
	}
	
	// 못 팔고 남아있는 매수 주문 있는지 확인
	@Scheduled(fixedDelay = 1000 * 60) 
		public void sellRemain() {
			for(CoinType coinType : CoinType.values()) {
				vloatilityRangeBreakoutSellService.sellRemainOrder(coinType);
			}
		}

}
