package com.invest.coin.domain.service.quant.momentum.volatility_range_breakout;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
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
import com.invest.coin.domain.service.upbit.trade.UpbitTradeService;
import com.invest.coin.domain.util.DateUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class VloatilityRangeBreakoutSellService {
	
	private Map<String, AtomicBoolean> checking = new HashMap<>();
	private Map<String, LocalDateTime> checkingSetTime = new HashMap<>();
	
	private final UpbitTradeService upbitTradeService;
	private final VloatilityRangeBreakoutRepository vloatilityRangeBreakoutRepository;
	
	@PostConstruct
    public void init() {
        for(CoinType coinType : CoinType.values()) {
        	checking.put(coinType.getUpbitTicker(), new AtomicBoolean());
        	checkingSetTime.put(coinType.getUpbitTicker(), LocalDateTime.now());
		}
    }
	
	public void setChecking(boolean value) {
		for(CoinType coinType : CoinType.values()) {
			if (value) {
				checkingSetTime.put(coinType.getUpbitTicker(), LocalDateTime.now());
			}
			checking.get(coinType.getUpbitTicker()).set(value);
		}
	}
	
	@Async
	public void sell(CoinType coinType) {
		log.debug("coinType : {} , sell", coinType.name());
		if (checking.get(coinType.getUpbitTicker()).get()) {
			if (LocalDateTime.now().isBefore(checkingSetTime.get(coinType.getUpbitTicker()).plusMinutes(5))) {
				return;
			}
		}
		checking.get(coinType.getUpbitTicker()).set(true);
		checkingSetTime.put(coinType.getUpbitTicker(), LocalDateTime.now());
		
		ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
		List<VloatilityRangeBreakout> vloatilityRangeBreakouts = vloatilityRangeBreakoutRepository.findByCoinTypeAndDateStringAndDatetimeId(coinType.name(), DateUtil.getDateString(now.minusDays(1)), String.valueOf(now.getHour()));
		if (null == vloatilityRangeBreakouts) {
			checking.get(coinType.getUpbitTicker()).set(false);
			return;
		}
		vloatilityRangeBreakouts.stream().forEach(
				vloatilityRangeBreakout -> {
					if (vloatilityRangeBreakout.getStatus().equals(VloatilityRangeBreakoutStatus.NOT_BREAKOUT.getCode())) {
						checking.get(coinType.getUpbitTicker()).set(false);
						return;
					}
					
					if (vloatilityRangeBreakout.getStatus().equals(VloatilityRangeBreakoutStatus.BREAKOUT_REQUEST.getCode())) {
						vloatilityRangeBreakout.setUpdatedAt(ZonedDateTime.now(ZoneId.of("Asia/Seoul")));
						vloatilityRangeBreakout.setStatus(VloatilityRangeBreakoutStatus.NOT_BREAKOUT.getCode());
						vloatilityRangeBreakoutRepository.save(vloatilityRangeBreakout);
						checking.get(coinType.getUpbitTicker()).set(false);
						return;
					}
					
					if (vloatilityRangeBreakout.getStatus().equals(VloatilityRangeBreakoutStatus.BUY_DONE.getCode())) {
						sellOrder(coinType, vloatilityRangeBreakout);
					}
				}
			);
		
		checking.get(coinType.getUpbitTicker()).set(false);
	}
	
	// 못 팔고 남아있는 매수 주문 있는지 확인
	public void sellRemainOrder(CoinType coinType) {
		log.debug("coinType : {} , sellRemainOrder", coinType.name());
		if (checking.get(coinType.getUpbitTicker()).get()) {
			if (LocalDateTime.now().isBefore(checkingSetTime.get(coinType.getUpbitTicker()).plusMinutes(5))) {
				return;
			}
		}
		checking.get(coinType.getUpbitTicker()).set(true);
		checkingSetTime.put(coinType.getUpbitTicker(), LocalDateTime.now());
		
		List<VloatilityRangeBreakout> remainVloatilityRangeBreakouts = vloatilityRangeBreakoutRepository.findByCoinTypeAndStatus(coinType.name(), VloatilityRangeBreakoutStatus.BUY_DONE.getCode());
		if (null == remainVloatilityRangeBreakouts) {
			checking.get(coinType.getUpbitTicker()).set(false);
			return;
		}
		
		ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
		int date = Integer.parseInt(DateUtil.getDateString(now.minusDays(1)));
		int datetime = now.getHour();
		
		remainVloatilityRangeBreakouts.stream().forEach(
				vloatilityRangeBreakout -> {
					int dataDate = Integer.parseInt(vloatilityRangeBreakout.getDateString());
					int dataDatetime = Integer.parseInt(vloatilityRangeBreakout.getDatetimeId());
					
					if (date > dataDate || (date == dataDate && datetime > dataDatetime)) {
						sellOrder(coinType, vloatilityRangeBreakout);
					}
				});
		
		checking.get(coinType.getUpbitTicker()).set(false);
	}
	
	public void sellOrder(CoinType coinType, VloatilityRangeBreakout vloatilityRangeBreakout) {
		UpbitOrder upbitSellOrder = upbitTradeService.sellMarketPrice(coinType, vloatilityRangeBreakout.getBuyCount());
		log.debug("coinType : {} , upbitSellOrder : {}", upbitSellOrder);
		
		vloatilityRangeBreakout.setSellUuid(upbitSellOrder.getUuid());
		vloatilityRangeBreakout.setUpdatedAt(ZonedDateTime.now(ZoneId.of("Asia/Seoul")));
		vloatilityRangeBreakout.setStatus(VloatilityRangeBreakoutStatus.SELL_REQUEST.getCode());
		vloatilityRangeBreakoutRepository.save(vloatilityRangeBreakout);
	}
	
	@Async
	public void confirmSellOrder(CoinType coinType) {
		log.debug("coinType : {} , confirmSellOrder", coinType.name());
		List<VloatilityRangeBreakout> vloatilityRangeBreakouts = vloatilityRangeBreakoutRepository.findByCoinTypeAndStatus(coinType.name(), VloatilityRangeBreakoutStatus.SELL_REQUEST.getCode());
		if (null == vloatilityRangeBreakouts || vloatilityRangeBreakouts.isEmpty()) {
			return;
		}
		
		vloatilityRangeBreakouts.stream().forEach(
				vloatilityRangeBreakout -> {
					
					UpbitOrder upbitSellOrderDetail = upbitTradeService.getOrder(vloatilityRangeBreakout.getSellUuid());
					log.debug("coinType : {} , upbitSellOrderDetail : {}", upbitSellOrderDetail);
					if (upbitSellOrderDetail.getState().equalsIgnoreCase("wait")) {
						return;
					}
					
					BigDecimal sellCount = BigDecimal.ZERO;
					BigDecimal sellAvgPrice = BigDecimal.ZERO;
					if (upbitSellOrderDetail.getTrades().size() == 1) {
						sellCount = upbitSellOrderDetail.getTrades().get(0).getVolume();
						sellAvgPrice = upbitSellOrderDetail.getTrades().get(0).getPrice();
					} else {
						sellCount = upbitSellOrderDetail.getTrades().stream()
				                .map(trade -> trade.getVolume())
				                .reduce(BigDecimal.ZERO, BigDecimal::add);
						
						BigDecimal sellSumPrice = upbitSellOrderDetail.getTrades().stream()
				                .map(trade -> trade.getVolume().multiply(trade.getPrice().setScale(8, RoundingMode.HALF_UP)))
				                .reduce(BigDecimal.ZERO, BigDecimal::add);
						
						sellAvgPrice = sellSumPrice.divide(sellCount, 8, RoundingMode.HALF_UP);
					}
					
					vloatilityRangeBreakout.setSellPrice(sellAvgPrice);
					vloatilityRangeBreakout.setSellCount(sellCount);
					vloatilityRangeBreakout.setFee(upbitSellOrderDetail.getPaidFee().add(vloatilityRangeBreakout.getFee()));
					vloatilityRangeBreakout.setSellAt(ZonedDateTime.now(ZoneId.of("Asia/Seoul")));
					vloatilityRangeBreakout.setUpdatedAt(ZonedDateTime.now(ZoneId.of("Asia/Seoul")));
					vloatilityRangeBreakout.setStatus(VloatilityRangeBreakoutStatus.SELL_DONE.getCode());
					vloatilityRangeBreakoutRepository.save(vloatilityRangeBreakout);
				});
		
	}

}
