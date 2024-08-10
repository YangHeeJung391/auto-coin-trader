package com.invest.coin.domain.service.quant.momentum.volatility_range_breakout;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.invest.coin.domain.entity.coin.VloatilityRangeBreakout;
import com.invest.coin.domain.model.CoinType;
import com.invest.coin.domain.model.quant.momentum.volatility_range_breakout.VloatilityRangeBreakoutStatus;
import com.invest.coin.domain.repository.coin.VloatilityRangeBreakoutRepository;
import com.invest.coin.domain.service.upbit.market.UpbitMarketService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class VloatilityRangeBreakoutStoplossService {
	
	public static double STOP_LOSS_PERCENT = 0.02;
	
	private Map<String, AtomicBoolean> checking = new HashMap<>();
	private Map<String, LocalDateTime> checkingSetTime = new HashMap<>();
	
	private final UpbitMarketService upbitMarketService;
	private final VloatilityRangeBreakoutRepository vloatilityRangeBreakoutRepository;
	private final VloatilityRangeBreakoutSellService vloatilityRangeBreakoutSellService;
	
	@PostConstruct
    public void init() {
        for(CoinType coinType : CoinType.values()) {
        	checking.put(coinType.getUpbitTicker(), new AtomicBoolean());
        	checkingSetTime.put(coinType.getUpbitTicker(), LocalDateTime.now());
		}
    }
	
	public void checkStopLossAndSellOrder(CoinType coinType) {
		log.debug("check stop loss");
		if (checking.get(coinType.getUpbitTicker()).get()) {
			if (LocalDateTime.now().isBefore(checkingSetTime.get(coinType.getUpbitTicker()).plusMinutes(5))) {
				return;
			}
		}
		checking.get(coinType.getUpbitTicker()).set(true);
		checkingSetTime.put(coinType.getUpbitTicker(), LocalDateTime.now());
		
		List<VloatilityRangeBreakout> vloatilityRangeBreakouts = vloatilityRangeBreakoutRepository.findByCoinTypeAndStatus(coinType.name(), VloatilityRangeBreakoutStatus.BUY_DONE.getCode());
		if (null == vloatilityRangeBreakouts || vloatilityRangeBreakouts.isEmpty()) {
			checking.get(coinType.getUpbitTicker()).set(false);
			return;
		}
		
		BigDecimal currentPrice = upbitMarketService.getCurrentPrice(coinType);
		vloatilityRangeBreakouts.stream().forEach(
				vloatilityRangeBreakout -> {
					BigDecimal stoplossPrice = vloatilityRangeBreakout.getBuyPrice().multiply(new BigDecimal(1- STOP_LOSS_PERCENT));
					log.debug("cointype : {}, datetime : {}, stop loss price : {} , current price : {}", coinType.name(), vloatilityRangeBreakout.getDateString() + vloatilityRangeBreakout.getDatetimeId(), stoplossPrice, currentPrice);
					if (currentPrice.compareTo(stoplossPrice) < 0) {
						log.debug("stop loss execute");
						vloatilityRangeBreakoutSellService.sellOrder(coinType, vloatilityRangeBreakout);
					}
				});
		checking.get(coinType.getUpbitTicker()).set(false);
		
	}
	
	public void setChecking(boolean value) {
		for(CoinType coinType : CoinType.values()) {
			if (value) {
				checkingSetTime.put(coinType.getUpbitTicker(), LocalDateTime.now());
			}
			checking.get(coinType.getUpbitTicker()).set(value);
		}
	}

}
