package com.invest.coin.domain.service.quant.momentum.volatility_range_breakout;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.invest.coin.domain.entity.coin.VloatilityRangeBreakout;
import com.invest.coin.domain.model.Balance;
import com.invest.coin.domain.model.CoinType;
import com.invest.coin.domain.model.MovingAverage;
import com.invest.coin.domain.model.quant.momentum.volatility_range_breakout.VloatilityRangeBreakoutStatus;
import com.invest.coin.domain.repository.coin.VloatilityRangeBreakoutRepository;
import com.invest.coin.domain.service.upbit.account.UpbitAccountService;
import com.invest.coin.domain.service.upbit.market.UpbitMarketService;
import com.invest.coin.domain.util.DateUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class VloatilityRangeBreakoutService {
	
	private final UpbitMarketService upbitMarketService;
	private final UpbitAccountService upbitAccountService;
	private final VloatilityRangeBreakoutRepository vloatilityRangeBreakoutRepository;
	
	public void calculate(CoinType coinType, BigDecimal targetVloatilityRate) {
		MovingAverage yesterdayMovingAverage = upbitMarketService.getYesterDayMovingAverage(coinType);
		BigDecimal yesterdayRange = yesterdayMovingAverage.getHighPrice().subtract(yesterdayMovingAverage.getLowPrice());
		BigDecimal noiseRate = getNoiseRate(yesterdayMovingAverage);
		
		BigDecimal currentPrice = upbitMarketService.getCurrentPrice(coinType);
		
		BigDecimal k = getK(coinType, noiseRate);
		
		BigDecimal investmentRate = getInvestmentRateByTargetVolatility(yesterdayMovingAverage, targetVloatilityRate);
		
		Balance balance = upbitAccountService.getUpbitBalance();
		BigDecimal buyableAmount = balance.getTotalAmount().divide(BigDecimal.valueOf(24 * CoinType.values().length), 8, RoundingMode.HALF_UP);
		BigDecimal targetPrice = currentPrice.add(yesterdayRange.multiply(k).setScale(8, RoundingMode.HALF_UP));
		BigDecimal movingAverageScore = getMovingAverageScore(coinType, targetPrice);
		
		BigDecimal buyAmount = buyableAmount.multiply(movingAverageScore).multiply(investmentRate);
		BigDecimal targetCount = buyAmount.divide(targetPrice, 8, RoundingMode.HALF_UP);
		
		ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
		VloatilityRangeBreakout vloatilityRangeBreakout = VloatilityRangeBreakout.builder()
		.coinType(coinType.name())
		.dateString(DateUtil.getDateString(now))
		.datetimeId(String.valueOf(now.getHour()))
		.openingPrice(yesterdayMovingAverage.getClosePrice())
		.movingAvgScore(movingAverageScore)
		.noiseRate(noiseRate)
		.targetPrice(targetPrice)
		.targetCount(targetCount)
		.k(k)
		.vloatilityInvestRate(investmentRate)
		.status((targetCount.compareTo(BigDecimal.ZERO) == 0)? VloatilityRangeBreakoutStatus.NOT_BREAKOUT.getCode() : VloatilityRangeBreakoutStatus.BREAKOUT_REQUEST.getCode())
		.createdAt(now).build();
		
		// DB 저장
		vloatilityRangeBreakoutRepository.save(vloatilityRangeBreakout);
	}
	
	private BigDecimal getK(CoinType coinType, BigDecimal currentNoiseRate) {
		ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
		List<VloatilityRangeBreakout> vloatilityRangeBreakouts = vloatilityRangeBreakoutRepository.findByCoinTypeAndDatetimeIdAndDateStringRange(
				coinType.name()
				, String.valueOf(now.getHour())
				, DateUtil.getDateString(now.minusDays(19)) );
		
		if(null != vloatilityRangeBreakouts && !vloatilityRangeBreakouts.isEmpty()) {
			BigDecimal totalNoiseRate = vloatilityRangeBreakouts.stream().map(VloatilityRangeBreakout::getNoiseRate).reduce(BigDecimal.ZERO, BigDecimal::add);
			totalNoiseRate = currentNoiseRate.add(totalNoiseRate);
			return totalNoiseRate.divide(BigDecimal.valueOf(vloatilityRangeBreakouts.size() + 1), 8, RoundingMode.HALF_UP);
		}
		
		return currentNoiseRate;
	}
	
	private BigDecimal getNoiseRate(MovingAverage movingAverage) {
		BigDecimal range1 = movingAverage.getOpeningPrice().subtract(movingAverage.getClosePrice()).abs();
		BigDecimal range2 = movingAverage.getHighPrice().subtract(movingAverage.getLowPrice());
		BigDecimal noiseRate = BigDecimal.valueOf(1).subtract(range1.divide(range2, 8, RoundingMode.HALF_UP));
		
		return noiseRate;
	}
	
	private BigDecimal getMovingAverageScore(CoinType cointType, BigDecimal targetPrice) {
		BigDecimal oneMovingAverageScore = BigDecimal.ONE.divide(BigDecimal.valueOf(18), 4, RoundingMode.HALF_UP);
		BigDecimal movingAverageScore = BigDecimal.ZERO;
		ZonedDateTime baseZoneDateTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).withMinute(0).withSecond(0).withNano(0);
		for(int i=20; i>=3; i--) {
			MovingAverage movingAverage = upbitMarketService.getDayMovingAverage(cointType, i, baseZoneDateTime);
			if(targetPrice.compareTo(movingAverage.getTradePrice()) > 0) {
				movingAverageScore = movingAverageScore.add(oneMovingAverageScore);
			}
		}
		if (movingAverageScore.compareTo(BigDecimal.ONE) > 0) {
			return BigDecimal.ONE;
		}
		return movingAverageScore;
	}
	
	private BigDecimal getInvestmentRateByTargetVolatility(MovingAverage yesterdayMovingAverage, BigDecimal targetVloatilityRate) {
		BigDecimal targetVolatility = targetVloatilityRate;
		BigDecimal yesterdayRange = yesterdayMovingAverage.getHighPrice().subtract(yesterdayMovingAverage.getLowPrice());
		BigDecimal yesterdayVolatility = yesterdayRange.divide(yesterdayMovingAverage.getClosePrice(),8, RoundingMode.HALF_UP);
		
		BigDecimal investmentRate = targetVolatility.divide(yesterdayVolatility, 8, RoundingMode.HALF_UP);
		if (investmentRate.compareTo(BigDecimal.ZERO) < 0) {
			return BigDecimal.ZERO;
		}
		if (investmentRate.compareTo(BigDecimal.ONE) > 0) {
			return BigDecimal.ONE;
		}
		
		return investmentRate;
	}

}
