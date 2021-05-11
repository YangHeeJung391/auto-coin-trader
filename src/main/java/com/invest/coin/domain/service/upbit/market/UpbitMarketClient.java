package com.invest.coin.domain.service.upbit.market;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.invest.coin.config.FeignRetryConfig;
import com.invest.coin.domain.model.upbit.market.UpbitMinuteCandle;
import com.invest.coin.domain.model.upbit.market.UpbitOrderbook;
import com.invest.coin.domain.model.upbit.market.UpbitTicker;

@FeignClient(name = "upbit-api", contextId="upbitMarketClient", url = "${upbit.host}")
public interface UpbitMarketClient {
	
	
	@GetMapping("/v1/ticker")
	ResponseEntity<List<UpbitTicker>> getTicker(@RequestParam("markets") String market);
		
	
	@GetMapping("/v1/candles/minutes/{minute}")
	ResponseEntity<List<UpbitMinuteCandle>> getMinuteCandle(@PathVariable("minute") int minute,
			@RequestParam("market") String market,
			@RequestParam("count") int count,
			@RequestParam("to") String to);
	
	
	@GetMapping("/v1/orderbook")
	ResponseEntity<List<UpbitOrderbook>> getOrderBook(@RequestParam("markets") String market);

}
