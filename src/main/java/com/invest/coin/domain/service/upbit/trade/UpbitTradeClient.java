package com.invest.coin.domain.service.upbit.trade;

import java.math.BigDecimal;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.invest.coin.config.FeignRetryConfig;
import com.invest.coin.domain.model.upbit.trade.UpbitOrder;
import com.invest.coin.domain.model.upbit.trade.UpbitOrderChance;

@FeignClient(name = "upbit-api", contextId="upbitTradeClient", url = "${upbit.host}")
public interface UpbitTradeClient {
	
	
	@PostMapping("/v1/orders")
	ResponseEntity<UpbitOrder> order(@RequestParam("market") String market,
			@RequestParam("side") String side,
			@RequestParam("volume") BigDecimal volume,
			@RequestParam(value = "price", required = false) BigDecimal price,
			@RequestParam("ord_type") String ordType,
			@RequestHeader("Authorization") String authorizationToken);
	
	
	@DeleteMapping("/v1/order")
	ResponseEntity<UpbitOrder> cancelOrder(@RequestParam("uuid") String uuid,
			@RequestHeader("Authorization") String authorizationToken);
	
	
	@GetMapping("/v1/order")
	ResponseEntity<UpbitOrder> getOrder(@RequestParam("uuid") String uuid,
			@RequestHeader("Authorization") String authorizationToken);


	
	@GetMapping("/v1/orders/chance")
	ResponseEntity<UpbitOrderChance> getOrderChance(@RequestParam("market") String market,
			@RequestHeader("Authorization") String authorizationToken);

}
