package com.invest.coin.domain.service.quant.momentum.volatility_range_breakout;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
import com.invest.coin.domain.model.upbit.trade.UpbitOrder;
import com.invest.coin.domain.repository.coin.VloatilityRangeBreakoutRepository;
import com.invest.coin.domain.service.upbit.market.UpbitMarketService;
import com.invest.coin.domain.service.upbit.trade.UpbitTradeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class VloatilityRangeBreakoutBuyService {
	
	private Map<String, AtomicBoolean> checking = new HashMap<>();
	
	private final UpbitMarketService upbitMarketService;
	private final UpbitTradeService upbitTradeService;
	private final VloatilityRangeBreakoutRepository vloatilityRangeBreakoutRepository;
	
	@PostConstruct
    public void init() {
        for(CoinType coinType : CoinType.values()) {
        	checking.put(coinType.getUpbitTicker(), new AtomicBoolean());
		}
    }
	
	public void checkAndBuy(CoinType coinType) {
		log.debug("coinType : {} , checkAndBuy", coinType.name());
		if (checking.get(coinType.getUpbitTicker()).get()) {
			return;
		}
		checking.get(coinType.getUpbitTicker()).set(true);
		List<VloatilityRangeBreakout> vloatilityRangeBreakouts = vloatilityRangeBreakoutRepository.findByCoinTypeAndStatus(coinType.name(), VloatilityRangeBreakoutStatus.BREAKOUT_REQUEST.getCode());
		if (null == vloatilityRangeBreakouts || vloatilityRangeBreakouts.isEmpty()) {
			checking.get(coinType.getUpbitTicker()).set(false);
			return;
		}
		
		BigDecimal currentPrice = upbitMarketService.getCurrentPrice(coinType);
		vloatilityRangeBreakouts.stream().forEach(
				vloatilityRangeBreakout -> {
					log.debug("cointype : {}, datetime : {}, targetPrice : {} , currentPrice : {}", coinType.name(), vloatilityRangeBreakout.getDateString() + vloatilityRangeBreakout.getDatetimeId(), vloatilityRangeBreakout.getTargetPrice(), currentPrice);
					if (currentPrice.compareTo(vloatilityRangeBreakout.getTargetPrice()) >= 0) {
						UpbitOrder upbitOrder = upbitTradeService.buyMarketPrice(coinType, vloatilityRangeBreakout.getTargetPrice().multiply(vloatilityRangeBreakout.getTargetCount()).setScale(8, RoundingMode.HALF_UP));
						log.debug("coinType : {} , upbitBuyOrder : {}", upbitOrder);
						
						vloatilityRangeBreakout.setBuyUuid(upbitOrder.getUuid());
						vloatilityRangeBreakout.setStatus(VloatilityRangeBreakoutStatus.BUY_REQURST.getCode());
						
						vloatilityRangeBreakoutRepository.save(vloatilityRangeBreakout);
					}
				});
		checking.get(coinType.getUpbitTicker()).set(false);
	}
	
	public void setChecking(boolean value) {
		for(CoinType coinType : CoinType.values()) {
			checking.get(coinType.getUpbitTicker()).set(value);
		}
	}
	
	@Async
	public void confirmBuyOrder(CoinType coinType) {
		log.debug("coinType : {} , confirmBuyOrder", coinType.name());
		List<VloatilityRangeBreakout> vloatilityRangeBreakouts = vloatilityRangeBreakoutRepository.findByCoinTypeAndStatus(coinType.name(), VloatilityRangeBreakoutStatus.BUY_REQURST.getCode());
		if (null == vloatilityRangeBreakouts || vloatilityRangeBreakouts.isEmpty()) {
			return;
		}
		
		vloatilityRangeBreakouts.stream().forEach(
				vloatilityRangeBreakout -> {
					UpbitOrder upbitOrderDetail = upbitTradeService.getOrder(vloatilityRangeBreakout.getBuyUuid());
					log.debug("coinType : {} , upbitBuyOrderDetail : {}", upbitOrderDetail);
					if (upbitOrderDetail.getState().equalsIgnoreCase("wait")) {
						return;
					}
					
					BigDecimal buyCount = BigDecimal.ZERO;
					BigDecimal buyAvgPrice = BigDecimal.ZERO;
					if (upbitOrderDetail.getTrades().size() == 1) {
						buyCount = upbitOrderDetail.getTrades().get(0).getVolume();
						buyAvgPrice = upbitOrderDetail.getTrades().get(0).getPrice();
					} else {
						buyCount = upbitOrderDetail.getTrades().stream()
				                .map(trade -> trade.getVolume())
				                .reduce(BigDecimal.ZERO, BigDecimal::add);
						
						BigDecimal buySumPrice = upbitOrderDetail.getTrades().stream()
				                .map(trade -> trade.getVolume().multiply(trade.getPrice().setScale(8, RoundingMode.HALF_UP)))
				                .reduce(BigDecimal.ZERO, BigDecimal::add);
						
						buyAvgPrice = buySumPrice.divide(buyCount, 8, RoundingMode.HALF_UP);
					}
					
					vloatilityRangeBreakout.setBuyPrice(buyAvgPrice);
					vloatilityRangeBreakout.setBuyCount(buyCount);
					vloatilityRangeBreakout.setBuyAt(ZonedDateTime.now(ZoneId.of("Asia/Seoul")));
					vloatilityRangeBreakout.setStatus(VloatilityRangeBreakoutStatus.BUY_DONE.getCode());
					vloatilityRangeBreakout.setFee(upbitOrderDetail.getPaidFee());
					
					vloatilityRangeBreakoutRepository.save(vloatilityRangeBreakout);
					
				});
		
	}

}
