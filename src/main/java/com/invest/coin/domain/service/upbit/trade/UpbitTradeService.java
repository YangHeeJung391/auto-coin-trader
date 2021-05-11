package com.invest.coin.domain.service.upbit.trade;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.invest.coin.domain.model.CoinType;
import com.invest.coin.domain.model.upbit.OrderType;
import com.invest.coin.domain.model.upbit.market.UpbitOrderbookUnit;
import com.invest.coin.domain.model.upbit.trade.UpbitOrder;
import com.invest.coin.domain.model.upbit.trade.UpbitOrderChance;
import com.invest.coin.domain.service.upbit.UpbitTokenService;
import com.invest.coin.domain.service.upbit.market.UpbitMarketService;
import com.invest.coin.domain.util.UpbitRequestLimitUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpbitTradeService {
	
	private final UpbitMarketService upbitMarketService;
	private final UpbitTradeClient upbitOrderClient;
	private final UpbitTokenService upbitTokenService;

	public UpbitOrder buyMarketPrice(CoinType coinType, BigDecimal price, BigDecimal count) {
		price = price.subtract(price.remainder(coinType.getPriceUnit()));

		String queryString = "market=" + coinType.getUpbitTicker() + "&side=" + OrderType.BID.getCode() + 
				"&volume=" + count + "&price=" + price + "&ord_type=" + "limit";

		ResponseEntity<UpbitOrder> orderResponse = upbitOrderClient.order(coinType.getUpbitTicker(), 
				OrderType.BID.getCode(), count, price, "limit", 
				upbitTokenService.createAuthToken(queryString));
		
		System.out.println(orderResponse.getBody());
		
		
		UpbitRequestLimitUtil.checkRequestAvailable(orderResponse.getHeaders());
		return orderResponse.getBody();
	}
	
	public UpbitOrder buyMarketPrice(CoinType coinType, BigDecimal price) {
		String queryString = "market=" + coinType.getUpbitTicker() + "&side=" + OrderType.BID.getCode() + 
				"&price=" + price + "&ord_type=" + "price";
		
		ResponseEntity<UpbitOrder> orderResponse = upbitOrderClient.order(coinType.getUpbitTicker(), 
				OrderType.BID.getCode(), null, price, "price", 
				upbitTokenService.createAuthToken(queryString));
		
		UpbitRequestLimitUtil.checkRequestAvailable(orderResponse.getHeaders());
		return orderResponse.getBody();
	}
	
	public UpbitOrder sellMarketPrice(CoinType coinType, BigDecimal count) {
		String queryString = "market=" + coinType.getUpbitTicker() + "&side=" + OrderType.ASK.getCode() + 
				"&volume=" + count + "&ord_type=" + "market";
		
		ResponseEntity<UpbitOrder> orderResponse = upbitOrderClient.order(coinType.getUpbitTicker(), 
				OrderType.ASK.getCode(), count, null, "market", 
				upbitTokenService.createAuthToken(queryString));
		
		UpbitRequestLimitUtil.checkRequestAvailable(orderResponse.getHeaders());
		return orderResponse.getBody();
	}

	public UpbitOrder cancelOrder(String uuid) {
		String queryString = "uuid=" + uuid;

		ResponseEntity<UpbitOrder> orderCancelResponse = upbitOrderClient.cancelOrder(uuid, 
		upbitTokenService.createAuthToken(queryString));

		UpbitRequestLimitUtil.checkRequestAvailable(orderCancelResponse.getHeaders());
		return orderCancelResponse.getBody();
	}

	public UpbitOrder getOrder(String uuid) {
		String queryString = "uuid=" + uuid;

		ResponseEntity<UpbitOrder> orderResponse = upbitOrderClient.getOrder(uuid, 
		upbitTokenService.createAuthToken(queryString));

		UpbitRequestLimitUtil.checkRequestAvailable(orderResponse.getHeaders());
		return orderResponse.getBody();
	}

	public UpbitOrderChance getOrderChanceInfo(CoinType coinType) {
		String queryString = "market=" + coinType.getUpbitTicker();

		ResponseEntity<UpbitOrderChance> orderChanceResponse = upbitOrderClient.getOrderChance(coinType.getUpbitTicker(), 
		upbitTokenService.createAuthToken(queryString));

		UpbitRequestLimitUtil.checkRequestAvailable(orderChanceResponse.getHeaders());
		return orderChanceResponse.getBody();
	}

}
