package com.invest.coin.domain.service.upbit.market;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.invest.coin.domain.model.CoinType;
import com.invest.coin.domain.model.MovingAverage;
import com.invest.coin.domain.model.upbit.market.UpbitMinuteCandle;
import com.invest.coin.domain.model.upbit.market.UpbitOrderbook;
import com.invest.coin.domain.model.upbit.market.UpbitOrderbookUnit;
import com.invest.coin.domain.model.upbit.market.UpbitTicker;
import com.invest.coin.domain.util.UpbitRequestLimitUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpbitMarketService {
	
	private final UpbitMarketClient upbitMarketClient;
	
	public BigDecimal getCurrentPrice(CoinType coinType) {
		ResponseEntity<List<UpbitTicker>> upbitTickerResponse = upbitMarketClient.getTicker(coinType.getUpbitTicker());
		UpbitRequestLimitUtil.checkRequestAvailable(upbitTickerResponse.getHeaders());
		return upbitTickerResponse.getBody().get(0).getTradePrice();
	}
	
	public UpbitOrderbookUnit getRecentMarketOrderbookUnit(CoinType coinType) {
		ResponseEntity<List<UpbitOrderbook>> upbitOrderbooksResponse = upbitMarketClient.getOrderBook(coinType.getUpbitTicker());
		UpbitRequestLimitUtil.checkRequestAvailable(upbitOrderbooksResponse.getHeaders());
		return upbitOrderbooksResponse.getBody().get(0).getOrderbookUnits().get(0);
	}
	
	public MovingAverage getYesterDayMovingAverage(CoinType coinType) {
		return getRecentDayMovingAverage(coinType, 1);
	}
	
	public MovingAverage getRecentDayMovingAverage(CoinType coinType, int days) {
		ZonedDateTime baseZoneDateTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).withMinute(0).withSecond(0).withNano(0);
		return getDayMovingAverage(coinType, days, baseZoneDateTime);
	}
	
	public MovingAverage getDayMovingAverage(CoinType coinType, int days, ZonedDateTime toDate) {
		int chunkCount = 5;
		int remainDays = days;
		BigDecimal totalTradePrice = BigDecimal.ZERO;
		ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
		MovingAverage movingAverage = new MovingAverage();
		ResponseEntity<List<UpbitMinuteCandle>> upbitMinuteCurrentCandlesResponse = upbitMarketClient.getMinuteCandle(60, coinType.getUpbitTicker(), 1, now.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
		UpbitRequestLimitUtil.checkRequestAvailable(upbitMinuteCurrentCandlesResponse.getHeaders());
		movingAverage.setClosePrice(upbitMinuteCurrentCandlesResponse.getBody().get(0).getOpeningPrice());
		do {
			
			ResponseEntity<List<UpbitMinuteCandle>> upbitMinuteCandlesResponse = upbitMarketClient.getMinuteCandle(60, coinType.getUpbitTicker(), 24 * ((remainDays > chunkCount)? chunkCount : remainDays), toDate.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
			UpbitRequestLimitUtil.checkRequestAvailable(upbitMinuteCandlesResponse.getHeaders());
			
			BigDecimal highPrice = upbitMinuteCandlesResponse.getBody().stream().map(UpbitMinuteCandle::getHighPrice).max(Comparator.naturalOrder()).get();
			if (null == movingAverage.getHighPrice() || movingAverage.getHighPrice().compareTo(highPrice) < 0) {
				movingAverage.setHighPrice(highPrice);
			}
			BigDecimal lowPrice = upbitMinuteCandlesResponse.getBody().stream().map(UpbitMinuteCandle::getLowPrice).min(Comparator.naturalOrder()).get();
			if (null == movingAverage.getLowPrice() || movingAverage.getLowPrice().compareTo(lowPrice) > 0) {
				movingAverage.setLowPrice(lowPrice);
			}
			
			BigDecimal tradePrice = upbitMinuteCandlesResponse.getBody().stream().map(UpbitMinuteCandle::getTradePrice).reduce(BigDecimal.ZERO, BigDecimal::add);
			totalTradePrice = totalTradePrice.add(tradePrice);
			remainDays = remainDays - chunkCount;
			toDate = toDate.minusDays(chunkCount);
			if (remainDays <= 0) {
				movingAverage.setOpeningPrice(upbitMinuteCandlesResponse.getBody().get(upbitMinuteCandlesResponse.getBody().size() - 1).getOpeningPrice());
			}
			
		} while (remainDays > 0);
		
		movingAverage.setTradePrice(totalTradePrice.divide(BigDecimal.valueOf(days * 24), 8, RoundingMode.HALF_UP));
		return movingAverage;
	}

}
