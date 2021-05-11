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
	
	// �ż� ���� üũ ( 1�ʿ� �ѹ� ���� )
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
	
	// �ż� ü�� Ȯ�� ( 1�п� �ѹ� ���� )
	@Scheduled(fixedDelay = 1000 * 60) 
	public void confirmBuyOrder() {
		for(CoinType coinType : CoinType.values()) {
			vloatilityRangeBreakoutBuyService.confirmBuyOrder(coinType);
		}
	}
	
	// �ŵ� ü�� Ȯ�� ( 1�п� �ѹ� ���� )
	@Scheduled(fixedDelay = 1000 * 60) 
	public void confirmSellOrder() {
		for(CoinType coinType : CoinType.values()) {
			vloatilityRangeBreakoutSellService.confirmSellOrder(coinType);
		}
	}
	
	// ���� ���� Ȯ�� ( 5�ʿ� �ѹ� ���� , ���Ű��� ���� 5% �̻� �սǽ� ���� )
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
	
	// �ż��� ���� ��� (�� ���� 1�ð�����)
	@Scheduled(cron = "0 1 * * * *") 
	public void calculate() {
		BigDecimal targetVloatilityRate = BigDecimal.valueOf(0.04);
		for(CoinType coinType : CoinType.values()) {
			vloatilityRangeBreakoutService.calculate(coinType, targetVloatilityRate);
		}
	}
	
	// �ŵ� (�� ���� 1�ð�����)
	@Scheduled(cron = "0 0 * * * *") 
	public void sell() {
		for(CoinType coinType : CoinType.values()) {
			vloatilityRangeBreakoutSellService.sell(coinType);
		}
	}
	
	// �� �Ȱ� �����ִ� �ż� �ֹ� �ִ��� Ȯ��
	@Scheduled(fixedDelay = 1000 * 60) 
		public void sellRemain() {
			for(CoinType coinType : CoinType.values()) {
				vloatilityRangeBreakoutSellService.sellRemainOrder(coinType);
			}
		}

}
