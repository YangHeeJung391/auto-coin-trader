package com.invest.coin.controller.webapi;

import java.math.BigDecimal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.invest.coin.domain.model.Balance;
import com.invest.coin.domain.model.CoinType;
import com.invest.coin.domain.model.MovingAverage;
import com.invest.coin.domain.model.upbit.market.UpbitOrderbookUnit;
import com.invest.coin.domain.model.upbit.trade.UpbitOrder;
import com.invest.coin.domain.service.upbit.account.UpbitAccountService;
import com.invest.coin.domain.service.upbit.market.UpbitMarketService;
import com.invest.coin.domain.service.upbit.trade.UpbitTradeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TestController {
	
	private final UpbitAccountService upbitAccountService;
	private final UpbitMarketService upbitMarketService;
	private final UpbitTradeService upbitTradeService;
	
	@GetMapping("/accounts")
	public Balance getBalance() {
		return upbitAccountService.getUpbitBalance();
	}
	
	@GetMapping("/orderbook")
	public UpbitOrderbookUnit getOrderbook() {
		return upbitMarketService.getRecentMarketOrderbookUnit(CoinType.BTC);
	}
	
	@GetMapping("/movingAverage")
	public MovingAverage getMovingAverage(int days) {
		return upbitMarketService.getRecentDayMovingAverage(CoinType.BTC, days);
	}
	
	@GetMapping("/buyOrder")
	public UpbitOrder buyOrder(BigDecimal count) {
		return upbitTradeService.buyMarketPrice(CoinType.BTC, count);
	}
	
	@GetMapping("/getOrder")
	public UpbitOrder buyOrder(String uuid) {
		return upbitTradeService.getOrder(uuid);
	}
	
	
	
	

}
